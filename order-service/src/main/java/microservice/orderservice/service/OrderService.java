package microservice.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.orderservice.dto.InventoryResponse;
import microservice.orderservice.dto.OrderItemsDto;
import microservice.orderservice.dto.OrderRequest;
import microservice.orderservice.event.OrderPlacedEvent;
import microservice.orderservice.model.Order;
import microservice.orderservice.repository.OrderRepository;
import microservice.orderservice.model.OrderItems;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClienBuilder;
    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItems> orderItems = orderRequest.getOrderList().stream().map(this::mapToDto).toList();
        order.setOrderItems(orderItems);

        List<String> skuCode = order.getOrderItems().stream().map(OrderItems::getSkuCode).toList();

        Observation inventoryServicObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServicObservation.lowCardinalityKeyValue("call", "inventory-service");

        return inventoryServicObservation.observe(() -> {
            InventoryResponse[] innInventoryResponses = webClienBuilder.build().get()
                    .uri("http://inventory-srvice/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCode).build())
                    .retrieve().bodyToMono(InventoryResponse[].class).block();
            boolean allProductInStock = Arrays.stream(innInventoryResponses).allMatch(InventoryResponse::isInStock);

            if (allProductInStock) {
                orderRepository.save(order);
                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
                return "Order Placed";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        });
    }

    private OrderItems mapToDto(OrderItemsDto orderItemsDto) {
        OrderItems orderItems = new OrderItems();
        orderItems.setPrice(orderItems.getPrice());
        orderItems.setQuantity(orderItems.getQuantity());
        orderItems.setSkuCode(orderItems.getSkuCode());
        return orderItems;
    }
}

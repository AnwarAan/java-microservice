package microservice.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import microservice.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

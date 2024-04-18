package microservice.productservice.util;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import microservice.productservice.model.Product;
import microservice.productservice.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() < 1) {
            Product product = new Product();
            product.setName("iPhone 6");
            product.setDescription("iPhone 6");
            product.setPrice(BigDecimal.valueOf(2000000));

            productRepository.save(product);
        }
    }
}

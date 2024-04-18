package microservice.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import microservice.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}

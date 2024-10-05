package catalogue.service;

import catalogue.endity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {
    Flux<Product> findAllProducts(String filter);

    Mono<Product> createProduct(String title, String details);

    Mono<Product> findProduct(UUID productId);

    Mono<Void> updateProduct(UUID  id, String title, String details);

    Mono<Void> deleteProduct(UUID id);
}
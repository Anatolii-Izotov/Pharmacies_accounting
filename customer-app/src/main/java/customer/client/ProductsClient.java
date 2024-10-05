package customer.client;

import customer.entity.Product;
import jakarta.validation.Payload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductsClient {
    Flux<Product> findAllProducts(String filter);

    Mono<Product> findProduct(String id);

    Mono<Product> createProduct(String title, String details);

    Mono<String> deleteProduct(String productId);
}

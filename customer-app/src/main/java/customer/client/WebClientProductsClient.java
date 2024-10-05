package customer.client;

import customer.controller.payload.NewProductPayload;
import customer.entity.Product;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class WebClientProductsClient implements ProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<Product> findAllProducts(String filter) {
        return this.webClient.get()
                .uri("/catalogue-api/products?filter={filter}", filter)
                .retrieve()
                .bodyToFlux(Product.class);
//                .doOnNext(product -> LoggerFactory.getLogger(WebClientProductsClient.class)
//                        .info("Received product: {}", product.id()));
    }

    @Override
    public Mono<Product> findProduct(String id) {
        return this.webClient.get()
                .uri("/catalogue-api/products/{productId}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<Product> createProduct(String title, String details) {
        return this.webClient
                .post()
                .uri("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new NewProductPayload(title, details)))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        // Логируем ошибку клиента (4xx)
                        System.err.println("Client error: " + ex.getMessage());
                    } else if (ex.getStatusCode().is5xxServerError()) {
                        // Логируем ошибку сервера (5xx)
                        System.err.println("Server error: " + ex.getMessage());
                    } else {
                        // Логируем другие ошибки
                        System.err.println("Error: " + ex.getMessage());
                    }
                });
    }

    @Override
    public Mono<String> deleteProduct(String productId) {
        return this.webClient
                .delete()
                .uri("catalogue-api/products/{productId}", productId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    // Логирование ошибки
                    System.err.println("Error deleting product: " + error.getMessage());
                });
    }
}

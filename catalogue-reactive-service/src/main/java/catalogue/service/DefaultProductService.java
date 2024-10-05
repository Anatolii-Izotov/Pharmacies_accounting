package catalogue.service;

import reactor.core.publisher.Flux;
import lombok.RequiredArgsConstructor;
import catalogue.endity.Product;
import catalogue.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Flux<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            return this.productRepository.findByTitleRegexIgnoreCase(filter);
        } else {
            return this.productRepository.findAll();
        }
    }

    @Override
    public Mono<Product> createProduct(String title, String details) {
        return this.productRepository.save(new Product(UUID.randomUUID(), title, details));
    }

    @Override
    public Mono<Product> findProduct(UUID productId) {
        return this.productRepository.findById(productId);
    }

    @Override
    public Mono<Void> updateProduct(UUID id, String title, String details) {
        return this.productRepository.findById(id)
                .flatMap(product -> {
                    product.setTitle(title);
                    product.setDetails(details);
                    return productRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .then();
    }
    @Override
    public Mono<Void> deleteProduct(UUID id) {
        return this.productRepository.deleteById(id);
    }
}
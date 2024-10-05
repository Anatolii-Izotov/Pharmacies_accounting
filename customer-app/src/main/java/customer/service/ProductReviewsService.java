package customer.service;

import customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewsService {
    Mono<ProductReview> createProductReview(String productId, int rating, String review);

    Flux<ProductReview> findProductReviewsForProduct(String productId);
}

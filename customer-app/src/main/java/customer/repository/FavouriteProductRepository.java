package customer.repository;

import customer.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductRepository {


    Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct);

    Mono<Void> deleteByProductId(String productId);

    Mono<FavouriteProduct> findByProductId(String productId);

    Flux<FavouriteProduct> findAll();
}

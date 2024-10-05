package customer.repository;

import customer.entity.FavouriteProduct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryFavouriteProductRepository implements FavouriteProductRepository {

    private final List<FavouriteProduct> favouriteProducts = Collections.synchronizedList(new LinkedList<>());


    @Override
    public Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct) {
        this.favouriteProducts.add(favouriteProduct);
        return Mono.just(favouriteProduct);
    }

    @Override
    public Mono<Void> deleteByProductId(String productId) {
        this.favouriteProducts.removeIf(favouriteProduct -> Objects.equals(favouriteProduct.getProductId(), productId));
        return Mono.empty();
    }

    @Override
    public Mono<FavouriteProduct> findByProductId(String productId) {
        return Flux.fromIterable(this.favouriteProducts)
                .filter(favouriteProduct -> Objects.equals(favouriteProduct.getProductId(), productId))
                .singleOrEmpty();
    }

    @Override
    public Flux<FavouriteProduct> findAll() {
        return Flux.fromIterable(this.favouriteProducts);
    }
}
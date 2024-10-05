package customer.service;


import customer.entity.FavouriteProduct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface FavouriteProductsService {

    Mono<FavouriteProduct> addProductToFavourites(String productId);
    Mono<Void> removeProductFromFavourites(String productId);
    Mono<FavouriteProduct> findFavouriteProductByProduct(String productId);

    Flux<FavouriteProduct> findFavouriteProducts();
}

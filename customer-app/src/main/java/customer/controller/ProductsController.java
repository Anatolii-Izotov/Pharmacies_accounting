package customer.controller;

import customer.client.BadRequestException;
import customer.client.ProductsClient;

import customer.controller.payload.NewProductPayload;
import customer.entity.FavouriteProduct;
import customer.entity.Product;
import customer.service.FavouriteProductsService;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.result.view.RedirectView;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products")
public class ProductsController {

    private final ProductsClient productsClient;

    private final FavouriteProductsService favouriteProductsService;

    @GetMapping("list")
    public Mono<String> getProductsListPage(Model model, @RequestParam(name = "filter", required = false) String filter) {
        //model.addAttribute("filter", filter); // вне реактивного стрима
        return this.productsClient.findAllProducts(filter)
                .collectList()
                .doOnNext(products -> {
                    model.addAttribute("products", products);
                    model.addAttribute("filter", filter);
                })
                .thenReturn("customer/products/list");
    }

    @GetMapping("create")
    public Mono<String> getNewProductPage() {
        return Mono.just("customer/products/new_product");
    }

    @PostMapping("create")
    public Mono<String> createNewProduct(NewProductPayload payload, Model model) {
        model.addAttribute("inFavourites", false);
        return this.productsClient.createProduct(payload.title(), payload.details())
                .map(product -> ("redirect:/customer/products/create"))
                .onErrorResume(BadRequestException.class, exception -> {
                    model.addAttribute("payload", payload);
                    model.addAttribute("errors", exception.getErrors());
                    return Mono.just("customer/products/new_product");
                })
                .doOnError(ex -> {
                    // Логируем общие ошибки
                    System.err.println("Error: " + ex.getMessage());
                    model.addAttribute("error", ex.getMessage());
                });
    }


    @GetMapping("favourites")
    public Mono<String> getFavouriteProductsPage(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("filter", filter); // вне реактивного стрима

        return this.favouriteProductsService.findFavouriteProducts()
                .map(FavouriteProduct::getProductId)
                .collectList()
                .flatMap(favouriteProducts -> this.productsClient.findAllProducts(filter)
                        .filter(product -> favouriteProducts.contains(product.id()))
                        .collectList()
                        .doOnNext(products -> model.addAttribute("products", products)))
                .thenReturn("customer/products/favourites");
    }
}
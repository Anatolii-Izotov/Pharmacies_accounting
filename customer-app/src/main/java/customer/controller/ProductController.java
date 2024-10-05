package customer.controller;

import customer.client.ProductsClient;

import customer.controller.payload.NewProductReviewPayload;
import customer.entity.FavouriteProduct;
import customer.entity.Product;
import customer.service.FavouriteProductsService;
import customer.service.ProductReviewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavouriteProductsService favouriteProductsService;

    private final ProductReviewsService productReviewsService;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") String id) {
        return this.productsClient.findProduct(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("customer.products.error.not_found")));
    }

    @GetMapping
    public Mono<String> getProductPage(@PathVariable("productId") String id, Model model) {
        model.addAttribute("inFavourites", false);
        return this.productReviewsService.findProductReviewsForProduct(id)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(
                        this.favouriteProductsService.findFavouriteProductByProduct(id)
                                .doOnNext(favouriteProduct -> model.addAttribute("inFavourites", true))
                                )
                .thenReturn("customer/products/product");
    }
    @PostMapping("delete")
    public Mono<String> deleteProduct(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.productsClient.deleteProduct(productId)
                        .thenReturn("redirect:/customer/products/list"));
    }

    @PostMapping("add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductsService.addProductToFavourites(productId)
                        .thenReturn("redirect:/customer/products/%s".formatted(productId)));
    }

    @PostMapping("remove-from-favourites")
    public Mono<String> removeProductFromFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductsService.removeProductFromFavourites(productId)
                        .thenReturn("redirect:/customer/products/%s".formatted(productId)));
    }

    @PostMapping("create-review")
    public Mono<String> createReview(@PathVariable("productId") String id, @Valid NewProductReviewPayload payload,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("inFavourites", false);
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return this.favouriteProductsService.findFavouriteProductByProduct(id)
                    .doOnNext(favouriteProduct -> model.addAttribute("inFavourites", true))
                    .thenReturn("customer/products/product");
        } else {
            return this.productReviewsService.createProductReview(id, payload.rating(), payload.review())
                    .thenReturn("redirect:/customer/products/%s".formatted(id));
        }

    }
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "errors/404";
    }
}

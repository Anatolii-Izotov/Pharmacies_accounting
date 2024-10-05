package catalogue.controller.products;

import catalogue.controller.payload.UpdateProductPayload;
import catalogue.endity.Product;
import catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
//import org.apache.coyote.Response;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-api/products/{productId}")

public class ProductRestController {

    private final ProductService productService;
    private final MessageSource messageSource;

    @GetMapping
    public Mono<ResponseEntity<Product>> findProduct(@PathVariable("productId") UUID productId) {
        return productService.findProduct(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping
    public Mono<ResponseEntity<?>> updateProduct(@PathVariable("productId") UUID productId,
                                           @Valid @RequestBody UpdateProductPayload payload,
                                           BindingResult bindingResult)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException excption) {
                throw excption;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            return this.productService.updateProduct(productId, payload.title(), payload.details())
                    .then(Mono.just(ResponseEntity.noContent().build()));
        }
    }
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable("productId") UUID productId) {
        return this.productService.deleteProduct(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

//    @ExceptionHandler(NoSuchElementException.class)
//    public Mono<ResponseEntity<ProblemDetail>> handleNoSuchElementException(NoSuchElementException exception, Locale locale) {
//        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
//                this.messageSource.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale))));
//    }
}

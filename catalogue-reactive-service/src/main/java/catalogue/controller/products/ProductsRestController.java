package catalogue.controller.products;

import catalogue.controller.payload.NewProductPayload;
import catalogue.endity.Product;
import catalogue.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping
    public Flux<Product> findProducts(@RequestParam(value = "filter", required = false) String filter) {
        return this.productService.findAllProducts(filter.toLowerCase());
    }

    @PostMapping
    public Mono<ResponseEntity<Product>> createProduct(@Valid @RequestBody Mono<NewProductPayload> payloadMono,
                                                 //BindingResult bindingResult,
                                                 UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
//        if (bindingResult.hasErrors()) {
//            if (bindingResult instanceof BindException excption) {
//                throw excption;
//            } else {
//                throw new BindException(bindingResult);
//            }
//        } else {
            return payloadMono
                    .flatMap(payload -> this.productService.createProduct(payload.title(), payload.details()))
                    .map(product -> ResponseEntity.created(uriComponentsBuilder.replacePath("/catalogue-api/products/{productId}")
                                    .build(product.getId()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(product));
        //}
//            return this.productService.createProduct(payload.title(), payload.details())
//                    .map(product -> ResponseEntity
//                            .created(uriComponentsBuilder
//                                    .path("/catalogue-api/products/{productId}")
//                                    .buildAndExpand(product.getId())
//                                    .toUri())
//                            .body(product));
//            Mono<Product> product = this.productService.createProduct(payload.title(), payload.details());
//            return Mono.just(ResponseEntity
//                    .created(uriComponentsBuilder
//                            .replacePath("/catalogue-api/products/{productId}")
//                            .build(Map.of("productId", product.getId())))
//                    .body(product));
    }
}


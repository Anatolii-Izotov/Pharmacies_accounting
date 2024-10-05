package manager.client;

import com.sun.source.tree.ParameterizedTypeTree;
import lombok.RequiredArgsConstructor;
import manager.controller.payload.NewProductPayload;
import manager.controller.payload.UpdateProductPayload;
import manager.endity.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductsRestClient implements ProductsRestClient {

    private final static ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            }; // получение доступа к типам, которые объявлены в <List<Product> в дженериках.


    private final RestClient restClient;

    @Override
    public List<Product> findAllProducts(String filter) {
        return this.restClient
                .get()
                .uri("/catalogue-api/products?filter={filter}", filter)
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(String title, String details) {
        try {
            return this.restClient
                    .post()
                    .uri("/catalogue-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayload(title, details))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
            /*
            TODO: проверка proporties это не null, что в proporties есть ключ errors и он является
                списком (массивом) строк
            */
        }

    }

    @Override
    public Optional<Product> findProduct(Long productId) {
        try {
            return Optional.ofNullable(this.restClient
                    .get()
                    .uri("catalogue-api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(Long productId, String title, String details) {
        try {
            this.restClient
                    .patch()
                    .uri("/catalogue-api/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductPayload(title, details))
                    .retrieve();
                    //.toBodilessEntity();
            /*
            если требуется доступ к ответу, можно вызвать toBodilessEntity()
            */
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
            /*
            TODO: проверка proporties это не null, что в proporties есть ключ errors и он является
                списком (массивом) строк
            */
        }

    }

    @Override
    public void deleteProduct(Long productId) {
        try {
            Optional.ofNullable(this.restClient
                    .delete()
                    .uri("catalogue-api/products/{productId}", productId)
                    .retrieve()
                    .toBodilessEntity());
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}

package manager.client;

import manager.endity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {
    List<Product> findAllProducts(String filter);

    Product createProduct(String title, String detail);

    Optional<Product> findProduct(Long productId);

    void updateProduct(Long productId, String title, String details);

    void deleteProduct(Long productId);
}

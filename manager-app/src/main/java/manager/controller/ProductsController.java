package manager.controller;

import lombok.RequiredArgsConstructor;
import manager.client.BadRequestException;
import manager.client.ProductsRestClient;
import manager.controller.payload.NewProductPayload;
import manager.endity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/*
Контроллер, который будет обрабатывать http запросы
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")

public class ProductsController {
    private final ProductsRestClient productsRestClient;

    // метод для получения списка товаров
    @GetMapping("list")
    // возвращает название шаблона, который должен быть срендерин в виде html страницы
    public String getProductsList(Model model,
                                  @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", this.productsRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload, Model model) {
        try {
            Product product = this.productsRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/new_product";
        }

    }
}

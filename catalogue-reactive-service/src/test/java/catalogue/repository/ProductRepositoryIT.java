//package catalogue.repository;
//
//import catalogue.endity.Product;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Iterator;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@Sql("/sql/products.sql")
//@Transactional
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class ProductRepositoryIT {
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @Test
//    void findAllByTitleLikeIgnoreCase_ReturnsFilteredProductsList() {
//        // given
//        var filter = "%Item №2%";
//
//        // when
//        var products = this.productRepository.findAllByTitleLikeIgnoreCase(filter);
//
//        // then
//        assertEquals(List.of(new Product("1asdf", "Item №2", "description of item №2")), products);
//    }
//}
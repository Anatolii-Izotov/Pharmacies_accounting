package catalogue.controller;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpHead;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Media;

import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProductsRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Sql("/sql/products.sql")
    @Test
    void findProduct_ReturnsProductsList() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue-api/products")
                .param("filter", "item")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        [
                        {"id": 1, "title": "Item №1", "details": "description of item №1"},
                        {"id": 3, "title": "Item №3", "details": "description of item №3"}
                        ]"""));
    }

    @Test
    void createProduct_RequestIsValid_ReturnsNewProduct() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "one more new item", "details": "some description of new item"}""")
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost/catalogue-api/products/1"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "id": 1,
                                "title": "one more new item",
                                "details": "some description of new item"
                                }"""));
    }
//    @Test
//    void createProduct_RequestIsInvalid_ReturnsProblemDetail() throws Exception {
//        // given
//        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("""
//                        {"title": "   ", "details": "null"}""")
//                .locale(Locale.of("ru", "RU"))
//                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));
//        // when
//        this.mockMvc.perform(requestBuilder)
//                // then
//                .andDo(print())
//                .andExpectAll(
//                        status().isBadRequest(),
//                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
//                        content().json("""
//                                {
//                                    "errors": [
//                                        "Name of the product must be from 3 to 50 symbols"
//                                ]
//                                }"""));
//    }
    @Test
    void createProductUserIsNotAuthorized_RequestIsInvalid_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "   ", "details": "null"}""")
                .locale(Locale.of("ru", "RU"))
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));
        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }
}
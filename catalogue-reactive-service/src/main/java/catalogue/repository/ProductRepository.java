package catalogue.repository;

import catalogue.endity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends ReactiveCrudRepository<Product, UUID> {

    // написание собственных запросов
    // вариант написания кастомных запросов в репозиториях
    // (так же можно использовать именованые запросы, которые описываются в классах сущностях)

    // JPQL запрос
    //@Query(value = "select p from Product p where p.title ilike :filter")
    // нативный запрос на языке sql
    //@Query(value = "select * from catalogue.t_product where c_title ilike :filter", nativeQuery = true)

    //@Query(name = "Product.findAllByTitleLikeIgnoringCase", nativeQuery = true)
    Flux<Product> findByTitleRegexIgnoreCase(@Param("filter") String filter);

    /*
    название данного метода преобразуется примерно в такой запрос:
    select * from catalogue.t_product where c_title ilike :filter
    */


}
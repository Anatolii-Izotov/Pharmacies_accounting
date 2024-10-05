package catalogue.repository;

import catalogue.endity.Medication;
import catalogue.endity.Product;
import catalogue.endity.Stock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface inStockRepository extends ReactiveMongoRepository<Stock, UUID> {
    Flux<Stock> findByQuantityRegex(@Param("filter") int filter);
}

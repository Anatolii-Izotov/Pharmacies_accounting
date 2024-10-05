package catalogue.repository;

import catalogue.endity.Medication;
import catalogue.endity.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface MedicationRepository extends ReactiveCrudRepository<Medication, UUID> {
    Flux<Medication> findByNameLikeIgnoreCase(@Param("filter") String filter);
}

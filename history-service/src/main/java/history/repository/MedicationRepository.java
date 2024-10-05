package history.repository;

import history.enity.Medication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface MedicationRepository extends ReactiveCrudRepository<Medication, UUID> {
}

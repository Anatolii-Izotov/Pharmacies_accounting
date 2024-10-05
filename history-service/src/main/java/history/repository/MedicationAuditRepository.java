package history.repository;

import history.enity.MedicationAudit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface MedicationAuditRepository extends ReactiveCrudRepository<MedicationAudit, UUID> {

}

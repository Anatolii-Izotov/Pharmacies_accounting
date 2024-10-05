package history.service;

import history.enity.MedicationAudit;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

public interface MedicationAuditService {

    Flux<MedicationAudit> findAllMedicationsChanges(String filter);

    Mono<MedicationAudit> medicationAuditEntry(UUID medicationId, String name, String description, String manufacturer,
                                               BigDecimal price, String category);
}

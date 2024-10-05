package history.service;

import history.enity.Medication;
import history.enity.MedicationAudit;
import history.repository.MedicationAuditRepository;
import history.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class HistoryMedicationAuditService implements MedicationAuditService {

    private final MedicationAuditRepository medicationAuditRepository;

    private final MedicationRepository medicationRepository;

    @Override
    public Flux<MedicationAudit> findAllMedicationsChanges(String filter) {
        return this.medicationAuditRepository.findAll(); //TODO: filter
    }

    @Override
    public Mono<MedicationAudit> medicationAuditEntry(UUID medicationId, String name, String description,
                                                      String manufacturer, BigDecimal price, String category) {
        return this.medicationRepository.findById(medicationId)
                .flatMap(medication -> this.medicationAuditRepository.save(new MedicationAudit(
                        UUID.randomUUID(), medication, name, description, manufacturer, price, category)));
    }
}

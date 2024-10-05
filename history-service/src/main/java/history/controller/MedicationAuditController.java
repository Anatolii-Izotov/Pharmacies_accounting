package history.controller;

import history.controller.payload.NewMedicationPayload;
import history.enity.MedicationAudit;
import history.service.MedicationAuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("history-api/medications")
public class MedicationAuditController {

    private final MedicationAuditService medicationAuditService;


    @GetMapping
    public Flux<MedicationAudit> findMedicationsChanges(@RequestParam(value = "filter", required = false) String filter) {
        return this.medicationAuditService.findAllMedicationsChanges(filter);
    }
    @PostMapping("/{medicationId}")
    public Mono<ResponseEntity<MedicationAudit>> updateMedicationAuditEntry(@PathVariable("medicationId") UUID medicationId,
                                                                      @Valid @RequestBody Mono<NewMedicationPayload> payloadMono) {
        return payloadMono
                .flatMap(payload -> this.medicationAuditService.medicationAuditEntry(medicationId, payload.name(),
                        payload.description(), payload.manufacturer(), payload.price(), payload.category()))
                .thenReturn(ResponseEntity.noContent().build());
    }

}

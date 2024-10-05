package catalogue.controller.medications;

import catalogue.controller.payload.NewStockMedicationPayload;
import catalogue.controller.payload.UpdateMedicationPayload;
import catalogue.controller.payload.UpdateMedicationInStockPayload;
import catalogue.endity.Medication;
import catalogue.endity.Stock;
import catalogue.service.medication.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/medications")
public class MedicationRestController {

    private final MedicationService medicationService;

    @GetMapping("/{medicationId}")
    public Mono<ResponseEntity<Medication>> findMedication(@PathVariable("medicationId") UUID medicationId) {
        return this.medicationService.findMedication(medicationId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{medicationId}")
    public Mono<ResponseEntity<?>> updateMedication(@PathVariable("medicationId") UUID medicationId,
                                                    @Valid @RequestBody Mono<UpdateMedicationPayload> payloadMono) throws BindException {

        return payloadMono
                .flatMap(payload -> this.medicationService.updateMedication(medicationId, payload.description(), payload.manufacturer(),
                        payload.price(), payload.category()))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping("/stock/{medicationId}")
    public Mono<ResponseEntity<Stock>> findMedicationInStock(@PathVariable("medicationId") UUID medicationInStockId) {
        return this.medicationService.findMedicationInStock(medicationInStockId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/stock/{medicationId}")
    public Mono<ResponseEntity<Stock>> addMedicationToStock(@PathVariable("medicationId") UUID medicationId,
                                                            @Valid @RequestBody Mono<NewStockMedicationPayload> payloadToStockMono,
                                                            UriComponentsBuilder uriComponentsBuilder) {
        return this.medicationService.findMedication(medicationId)
                .flatMap(medication -> payloadToStockMono
                        .flatMap(payloadToStock -> this.medicationService.addToStock(
                                medication,
                                payloadToStock.quantity(),
                                payloadToStock.expirationDate(),
                                payloadToStock.locationType(),
                                payloadToStock.location(),
                                payloadToStock.batchNumber(),
                                payloadToStock.dateReceived())))
                .map(medicationInStock -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/catalogue-api/medications/stock/{medicationId}")
                                .build(medicationInStock.getMedication().getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(medicationInStock));
    }

    @PatchMapping("/stock/{medicationInStockId}")
    public Mono<ResponseEntity<?>> updateMedicationInStock(@PathVariable("medicationInStockId") UUID medicationInStockId,
                                                           @Valid @RequestBody Mono<UpdateMedicationInStockPayload> payloadMono) throws BindException {

        return payloadMono
                .flatMap(payload -> this.medicationService.updateMedicationInStock(medicationInStockId, payload.quantity(), payload.expirationDate(),
                        payload.locationType(), payload.location(), payload.batchNumber(), payload.dateReceived()))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }


    @DeleteMapping("/delete/{medicationId}")
    public Mono<ResponseEntity<Void>> deleteMedication(@PathVariable("medicationId") UUID medicationId) {
        return this.medicationService.deleteMedication(medicationId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/stock/delete/{medicationInStockId}")
    public Mono<ResponseEntity<Void>> deleteMedicationFromStock(@PathVariable("medicationInStockId") UUID medicationInStockId) {
        return this.medicationService.deleteMedicationFromStock(medicationInStockId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

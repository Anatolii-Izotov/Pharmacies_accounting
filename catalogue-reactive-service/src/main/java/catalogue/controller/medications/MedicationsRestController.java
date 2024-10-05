package catalogue.controller.medications;

import catalogue.controller.payload.NewMedicationPayload;
import catalogue.controller.payload.NewProductPayload;
import catalogue.controller.payload.NewStockMedicationPayload;
import catalogue.endity.Medication;
import catalogue.endity.Stock;
import catalogue.service.ProductService;
import catalogue.service.medication.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/medications")
public class MedicationsRestController {

    private final MedicationService medicationService;

    @GetMapping
    public Flux<Medication> findMedications(@RequestParam(value = "filter", required = false) String filter) {
        return this.medicationService.findAllMedications(filter);
    }

    @GetMapping("/stock")
    public Flux<Stock> findAllMedicationsInStock(@RequestParam(value = "filter", required = false) String filter) {
        return this.medicationService.findAllMedicationsInStock(filter);
    }

    @PostMapping
    public Mono<ResponseEntity<Medication>> createMedication(@Valid @RequestBody Mono<NewMedicationPayload> payloadMono,
                                                          //BindingResult bindingResult,
                                                          UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
//        if (bindingResult.hasErrors()) {
//            if (bindingResult instanceof BindException excption) {
//                throw excption;
//            } else {
//                throw new BindException(bindingResult);
//            }
//        } else {
        return payloadMono
                .flatMap(payload -> this.medicationService.createMedication(payload.name(), payload.description(),
                        payload.manufacturer(), payload.price(), payload.category()))
                .map(medication -> ResponseEntity.created(uriComponentsBuilder.replacePath("/catalogue-api/medications/{medicationId}")
                                .build(medication.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(medication));
    }
}



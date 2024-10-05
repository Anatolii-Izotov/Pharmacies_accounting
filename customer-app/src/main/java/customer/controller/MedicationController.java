package customer.controller;


import customer.client.MedicationsClient;
import customer.controller.payload.NewStockMedicationPayload;
import customer.controller.payload.UpdateMedicationInStockPayload;
import customer.controller.payload.UpdateMedicationPayload;
import customer.entity.Medication;
import customer.entity.Stock;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/medications")
public class MedicationController {

    private final MedicationsClient medicationsClient;

    @GetMapping("/{medicationId}")
    public Mono<String> getMedicationPage(@PathVariable("medicationId") String id, Model model) {
        return this.medicationsClient.findMedication(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("customer.products.error.not_found")))
                .flatMap(medication -> {
                    model.addAttribute("medication", medication);
                    return checkMedicationInStock(this.medicationsClient.findMedication(id),
                            this.medicationsClient.findStockByMedication(id))
                            .defaultIfEmpty(false)
                            .flatMap(isInStock -> {
                                model.addAttribute("inStock", isInStock);
                                return Mono.just("customer/medications/medication");
                            });
                });
    }

    @GetMapping("/{medicationId}/edit")
    public Mono<String> getUpdateMedicationPage(@PathVariable("medicationId") String medicationId, Model model) {
        return this.medicationsClient.findMedication(medicationId)
                .flatMap(medication -> {
                    return Mono.fromCallable(() -> {
                        model.addAttribute("medication", medication);
                        return "customer/medications/edit";
                    });
                });
    }

    @GetMapping("/stock/{medicationId}/edit")
    public Mono<String> getMedicationInStockEditPage(@PathVariable("medicationId") String medicationId, Model model) {
        return this.medicationsClient.findMedication(medicationId)
                .map(medication -> model.addAttribute("medication", medication))
                .then(this.medicationsClient.findStockByMedication(medicationId)
                        .map(stock -> model.addAttribute("stock", stock))
                        .thenReturn("customer/medications/stock/edit"));
    }

    @PostMapping("/{medicationId}/edit")
    public Mono<String> updateMedication(@PathVariable("medicationId") String medicationId,
                                         UpdateMedicationPayload payload, Model model) {
        //model.addAttribute("medication", this.medicationsClient.findMedication(medicationId));

        Logger logger = LoggerFactory.getLogger(MedicationController.class);

        logger.info("Updating medication with ID: {}", medicationId);
        logger.info("Payload: {}", payload);

        return this.medicationsClient.findMedication(medicationId)
                .map(medication -> model.addAttribute("medication", medication))
                .then(this.medicationsClient.updateMedication(medicationId, payload.description(), payload.manufacturer(),
                                payload.price(), payload.category())
                        .thenReturn("redirect:/customer/medications/%s".formatted(medicationId)));
    }

    @PostMapping("/stock/{medicationId}")
    public Mono<String> addMedicationToStock(@PathVariable("medicationId") String medicationId, NewStockMedicationPayload stockPayload,
                                             Model model) {
        return this.medicationsClient.findMedication(medicationId)
                .flatMap(medication -> {
                    model.addAttribute("inStock", true);
                    return this.medicationsClient.addMedicationToStock(
                                    medication,
                                    stockPayload.quantity(),
                                    stockPayload.expirationDate(),
                                    stockPayload.locationType(),
                                    stockPayload.location(),
                                    stockPayload.batchNumber(),
                                    stockPayload.dateReceived())
                            .thenReturn("redirect:/customer/medications/%s".formatted(medicationId));
                });
    }

    @PostMapping("/stock/{medicationId}/edit")
    public Mono<String> updateMedicationInStock(@PathVariable("medicationId") String medicationId,
                                                Mono<UpdateMedicationInStockPayload> payloadStockMono,
                                                Mono<UpdateMedicationPayload> payloadMedicationMono,
                                                Model model) {
        Logger logger = LoggerFactory.getLogger(MedicationController.class);
        logger.info("Received request to update medication in stock for ID: {}", medicationId);

        return this.medicationsClient.findMedication(medicationId)
                .flatMap(medication -> {
                    model.addAttribute("medication", medication);
                    return this.medicationsClient.findStockByMedication(medicationId)
                            .flatMap(stock -> {
                                model.addAttribute("stock", stock);
                                return payloadMedicationMono
                                        .flatMap(payloadMedication -> this.medicationsClient.updateMedication(
                                                medicationId,
                                                payloadMedication.description(),
                                                payloadMedication.manufacturer(),
                                                payloadMedication.price(),
                                                payloadMedication.category()))
                                        .then(payloadStockMono
                                                .flatMap(payloadInStock -> this.medicationsClient.updateMedicationInStock(
                                                        //medication,
                                                        medicationId,
                                                        payloadInStock.quantity(),
                                                        payloadInStock.expirationDate(),
                                                        payloadInStock.locationType(),
                                                        payloadInStock.location(),
                                                        payloadInStock.batchNumber(),
                                                        payloadInStock.dateReceived())))
                                        .thenReturn("redirect:/customer/medications/%s".formatted(medicationId));
                            });
                });

//        return payloadStockMono
//                .flatMap(payloadInStock -> this.medicationsClient.findMedication(medicationId)
//                        .flatMap(medication -> {
//                            model.addAttribute("medication", medication);
//                            return payloadMedicationMono
//                                    .map(payloadMedication -> this.medicationsClient.updateMedication(
//                                            medicationId,
//                                            payloadMedication.description(),
//                                            payloadMedication.manufacturer(),
//                                            payloadMedication.price(),
//                                            payloadMedication.category()))
//                                    .then(this.medicationsClient.updateMedicationInStock(
//                                                    medication,
//                                                    payloadInStock.quantity(),
//                                                    payloadInStock.expirationDate(),
//                                                    payloadInStock.locationType(),
//                                                    payloadInStock.location(),
//                                                    payloadInStock.batchNumber(),
//                                                    payloadInStock.dateReceived())
//                                            .thenReturn("redirect:/customer/medications/%s".formatted(medicationId)));
//                        }));
    }

    @PostMapping("/delete/{medicationId}")
    public Mono<String> deleteMedication(@PathVariable("medicationId") String medicationId) {
        return this.medicationsClient.deleteMedication(medicationId)
                .thenReturn("redirect:/customer/medications/list");
    }

    @PostMapping("/stock/delete/{medicationId}")
    public Mono<String> deleteMedicationFromStock(@PathVariable("medicationId") String medicationId) {
        return this.medicationsClient.findStockByMedication(medicationId)
                .flatMap(stock -> this.medicationsClient.deleteMedicationFromStock(stock.id()))
                .thenReturn("redirect:/customer/medications/%s".formatted(medicationId));
    }

    private Mono<Boolean> checkMedicationInStock(Mono<Medication> medicationMono, Mono<Stock> stockMono) {
        return stockMono
                .zipWith(medicationMono)
                .map(tuple -> tuple.getT1().medication().id().equals(tuple.getT2().id()));
    }
}
package customer.client;

import customer.entity.Medication;
import customer.entity.Stock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface MedicationsClient {
    Flux<Medication> findAllMedications(String filter);

    Mono<Medication> createMedication(String name, String description, String manufacturer,
                                      BigDecimal price, String category);

    Mono<Medication> findMedication(String id);

    Mono<Void> updateMedication(String medicationId, String description, String manufacturer, BigDecimal price,
                                String category);

    Mono<String> deleteMedication(String productId);

    Flux<Stock> findAllMedicationsInStock(String filter);

    Mono<Stock> findStockByMedication(String medicationId);

    Mono<Stock> addMedicationToStock(Medication medication, int quantity, LocalDate expirationDate,
                                     String locationType, String location, String batchNumber, LocalDate dateReceived);

    Mono<Void> updateMedicationInStock(String medicationId, int quantity, LocalDate expirationDate, String locationType,
                                       String location, String batchNumber, LocalDate dateReceived);

    Mono<String> deleteMedicationFromStock(String medicationInStockId);


}
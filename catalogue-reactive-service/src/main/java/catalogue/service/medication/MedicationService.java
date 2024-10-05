package catalogue.service.medication;

import catalogue.endity.Medication;
import catalogue.endity.Stock;
import io.netty.util.AsyncMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface MedicationService {
    Flux<Medication> findAllMedications(String filter);

    Mono<Medication> createMedication(String name, String description, String manufacturer,
                                      BigDecimal price, String category);

    Mono<Medication> findMedication(UUID medicationId);

    Mono<Void> updateMedication(UUID medicationId, String description, String manufacturer, BigDecimal price, String category);


    Flux<Stock> findAllMedicationsInStock(String filter);

    Mono<Stock> addToStock(Medication medication, int quantity, LocalDate expirationDate, String localType,
                           String location, String batchNumber, LocalDate dateReceived);

    Mono<Stock> findMedicationInStock(UUID medicationInStockId);

    Mono<Void> updateMedicationInStock(UUID medicationInStockId, int quantity, LocalDate expirationDate,
                                       String locationType, String location, String batchNumber, LocalDate dateReceived);

    Mono<Void> deleteMedication(UUID id);

    Mono<Void> deleteMedicationFromStock(UUID medicationInStockId);


}

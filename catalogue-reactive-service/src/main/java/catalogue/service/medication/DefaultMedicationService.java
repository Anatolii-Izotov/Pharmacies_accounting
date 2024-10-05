package catalogue.service.medication;

import catalogue.endity.Medication;
import catalogue.endity.Stock;
import catalogue.repository.MedicationRepository;
import catalogue.repository.inStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultMedicationService implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final inStockRepository inStockRepository;

    @Override
    public Flux<Medication> findAllMedications(String filter) {

        if (filter != null && !filter.isBlank()) {
            return this.medicationRepository.findByNameLikeIgnoreCase(filter);
        } else {
            return this.medicationRepository.findAll();
        }
    }

    @Override
    public Mono<Medication> findMedication(UUID productId) {
        return this.medicationRepository.findById(productId);
    }

    @Override
    public Mono<Void> updateMedication(UUID medicationId, String description, String manufacturer, BigDecimal price, String category) {
        return this.medicationRepository.findById(medicationId)
                .flatMap(medication -> {
                    medication.setDescription(description);
                    medication.setManufacturer(manufacturer);
                    medication.setPrice(price);
                    medication.setCategory(category);
                    return this.medicationRepository.save(medication);
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .then();
    }

    @Override
    public Flux<Stock> findAllMedicationsInStock(String filter) {
        return this.inStockRepository.findAll()
                .flatMap(stock -> {
                    System.out.println("Found stock: " + stock);
                    return medicationRepository.findById(stock.getMedication().getId())
                            .map(medication -> {
                                stock.setMedication(medication);
                                return stock;
                            })
                            .doOnError(e -> System.out.println("Error finding medication: " + e.getMessage()));
                })
                .doOnError(e -> System.out.println("Error processing stock: " + e.getMessage()));

    }

    @Override
    public Mono<Stock> findMedicationInStock(UUID medicationInStockId) {
        return this.inStockRepository.findById(medicationInStockId);
    }

    @Override
    public Mono<Void> updateMedicationInStock(UUID medicationInStockId, int quantity, LocalDate expirationDate,
                                              String locationType, String location, String batchNumber, LocalDate dateReceived) {
        return this.inStockRepository.findById(medicationInStockId)
                .flatMap(medicationInStock -> {
                    //medicationInStock.setMedication(medication);
                    medicationInStock.setQuantity(quantity);
                    medicationInStock.setExpirationDate(expirationDate);
                    medicationInStock.setLocationType(locationType);
                    medicationInStock.setLocation(location);
                    medicationInStock.setBatchNumber(batchNumber);
                    medicationInStock.setDateReceived(dateReceived);
                    return this.inStockRepository.save(medicationInStock);
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .then();
    }

    @Override
    public Mono<Medication> createMedication(String name, String description, String manufacturer,
                                             BigDecimal price, String category) {
        return this.medicationRepository.save(new Medication(
                UUID.randomUUID(), name, description, manufacturer, price, category));
    }

    @Override
    public Mono<Void> deleteMedication(UUID id) {
        return this.medicationRepository.deleteById(id);
    }

    @Override
    public Mono<Stock> addToStock(Medication medication, int quantity, LocalDate expirationDate, String localType,
                                  String location, String batchNumber, LocalDate dateReceived) {
        return this.inStockRepository.save(new Stock(UUID.randomUUID(), medication, quantity, expirationDate, localType,
                location, batchNumber, dateReceived));
    }

    @Override
    public Mono<Void> deleteMedicationFromStock(UUID medicationInStockId) {
        return this.inStockRepository.deleteById(medicationInStockId);
    }
}

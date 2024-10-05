package customer.client;

import customer.controller.payload.NewMedicationPayload;
import customer.controller.payload.NewStockMedicationPayload;
import customer.controller.payload.UpdateMedicationInStockPayload;
import customer.controller.payload.UpdateMedicationPayload;
import customer.entity.Medication;
import customer.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class WebClientMedicationsClient implements MedicationsClient {

    private final WebClient webClient;
    @Override
    public Flux<Medication> findAllMedications(String filter) {
        return this.webClient.get()
                .uri("/catalogue-api/medications?filter={filter}", filter)
                .retrieve()
                .bodyToFlux(Medication.class);
    }

    @Override
    public Mono<Medication> findMedication(String id) {
        return this.webClient.get()
                .uri("/catalogue-api/medications/{medicationId}", id)
                .retrieve()
                .bodyToMono(Medication.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<Void> updateMedication(String medicationId, String description, String manufacturer, BigDecimal price, String category) {
        return this.webClient
                .patch()
                .uri("/catalogue-api/medications/{medicationId}", medicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UpdateMedicationPayload(description, manufacturer, price, category)))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(aVoid -> System.out.println("Medication updated successfully"))
                .doOnError(throwable -> System.err.println("Error updating medication: " + throwable.getMessage()));
    }

    @Override
    public Flux<Stock> findAllMedicationsInStock(String filter) {
        return this.webClient.get()
                .uri("/catalogue-api/medications/stock?filter={filter}", filter)
                .retrieve()
                .bodyToFlux(Stock.class);
    }

    @Override
    public Mono<Stock> findStockByMedication(String medicationId) {
        return findAllMedicationsInStock("")
                .filterWhen(stock -> Mono.just(stock.medication().id().equals(medicationId)))
                .next();
    }

    @Override
    public Mono<Medication> createMedication(String name, String description, String manufacturer,
                                             BigDecimal price, String category) {
        return this.webClient
                .post()
                .uri("/catalogue-api/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new NewMedicationPayload(name, description, manufacturer,
                        price, category)))
                .retrieve()
                .bodyToMono(Medication.class);
    }

    @Override
    public Mono<Stock> addMedicationToStock(Medication medication, int quantity, LocalDate expirationDate,
                                            String locationType, String location, String batchNumber, LocalDate dateReceived) {
        return this.webClient
                .post()
                .uri("/catalogue-api/medications/stock/%s".formatted(medication.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new NewStockMedicationPayload(medication, quantity, expirationDate,
                        locationType, location, batchNumber, dateReceived)))
                .retrieve()
                .bodyToMono(Stock.class);
    }

    @Override
    public Mono<Void> updateMedicationInStock(String medicationId, int quantity, LocalDate expirationDate,
                                              String locationType, String location, String batchNumber,
                                              LocalDate dateReceived) {
        return findStockByMedication(medicationId)
                .flatMap(stock ->
                    this.webClient
                            .patch()
                            .uri("/catalogue-api/medications/stock/%s".formatted(stock.id()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(new UpdateMedicationInStockPayload(quantity, expirationDate, locationType, location,
                                    batchNumber, dateReceived)))
                            .retrieve()
                            .bodyToMono(Void.class));
    }


    @Override
    public Mono<String> deleteMedication(String medicationId) {
        return this.webClient
                .delete()
                .uri("/catalogue-api/medications/{medicationId}", medicationId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    System.err.println("Error deleting medication: " + error.getMessage());
                });
    }

    @Override
    public Mono<String> deleteMedicationFromStock(String medicationInStockId) {
        return this.webClient
                .delete()
                .uri("/catalogue-api/medications/stock/delete/{medicationInStockId}", medicationInStockId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    System.err.println("Error deleting medication: " + error.getMessage());
                });
    }
}

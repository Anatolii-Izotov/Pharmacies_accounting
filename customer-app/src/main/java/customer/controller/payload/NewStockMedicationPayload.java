package customer.controller.payload;

import customer.entity.Medication;

import java.time.LocalDate;
import java.util.UUID;

public record NewStockMedicationPayload(Medication medication, int quantity, LocalDate expirationDate,
                                        String locationType, String location, String batchNumber, LocalDate dateReceived) {}

package customer.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record Stock(String id, Medication medication, int quantity, LocalDate expirationDate,
                    String locationType, String location, String batchNumber, LocalDate dateReceived) {}
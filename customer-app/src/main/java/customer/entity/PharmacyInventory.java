package customer.entity;

import java.time.LocalDate;
import java.util.UUID;

public record PharmacyInventory(UUID id, Medication medication, int quantity, LocalDate expirationDate) {
}

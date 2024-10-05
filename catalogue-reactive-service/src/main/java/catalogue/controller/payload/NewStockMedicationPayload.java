package catalogue.controller.payload;

import catalogue.endity.Medication;
import catalogue.endity.Supplier;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.UUID;

public record NewStockMedicationPayload(

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 0, message = "Quantity must be at least 0")
        int quantity,

        @NotNull(message = "Expiration date cannot be null")
        LocalDate expirationDate,

        @Size(max = 50, message = "Location type must be at most 50 characters")
        String locationType,

        @Size(max = 100, message = "Location must be at most 100 characters")
        String location,

        @Size(max = 50, message = "Batch number must be at most 50 characters")
        String batchNumber,

        @NotNull
        LocalDate dateReceived
        ) {
}

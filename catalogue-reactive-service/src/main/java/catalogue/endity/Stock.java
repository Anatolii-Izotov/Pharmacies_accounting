package catalogue.endity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock")
public class Stock {

    @Id
    private UUID id;

    //@NotNull(message = "Medication reference cannot be null")
    private Medication medication;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantity;

    @NotNull(message = "Expiration date cannot be null")
    private LocalDate expirationDate;

    @Size(max = 50, message = "Location type must be at most 50 characters")
    private String locationType;

    @Size(max = 100, message = "Location must be at most 100 characters")
    private String location;

    @Size(max = 50, message = "Batch number must be at most 50 characters")
    private String batchNumber;

    private LocalDate dateReceived;
}

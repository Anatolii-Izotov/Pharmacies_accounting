package catalogue.endity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sales")
public class Sale {

    @Id
    private UUID id;


    private Medication medication;

    @NotNull
    @Min(0)
    private int quantity;

    @NotNull
    private ZonedDateTime saleDate;

    @NotNull
    @Min(0)
    private BigDecimal totalPrice;
}

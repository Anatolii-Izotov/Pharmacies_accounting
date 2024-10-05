package history.enity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit")
public class MedicationAudit {

    @Id
    private UUID id;

    private Medication medication;

    @NotNull
    @Size(min = 3, max = 50)
    private String newName;

    @NotNull
    @Size(max = 2000)
    private String newDescription;

    @NotNull
    @Size(min = 3, max = 50)
    private String newManufacturer;

    @NotNull
    @Min(value = 0)
    private BigDecimal newPrice;

    @NotNull
    @Size(min = 3, max = 50)
    private String newCategory;
}

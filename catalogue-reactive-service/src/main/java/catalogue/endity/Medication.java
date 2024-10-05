package catalogue.endity;

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
@Document(collection = "medications")
public class Medication {

    @Id
    private UUID id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(max = 2000)
    private String description;

    @NotNull
    @Size(min = 3, max = 50)
    private String manufacturer;

    @NotNull
    @Min(value = 0)
    private BigDecimal price;

    @NotNull
    @Size(min = 3, max = 50)
    private String category;
}

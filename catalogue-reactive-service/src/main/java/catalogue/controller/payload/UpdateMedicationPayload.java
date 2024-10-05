package catalogue.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateMedicationPayload(


        @Size(max = 2000)
        String description,


        @Size(min = 3, max = 50)
        String manufacturer,


        @Min(value = 0)
        BigDecimal price,


        @Size(min = 3, max = 50)
        String category) {
}

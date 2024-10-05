package catalogue.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

public record NewMedicationPayload(
        @NotNull
        @Size(min = 3, max = 50)
        String name,

        @NotNull
        @Size(max = 2000)
        String description,

        @NotNull
        @Size(min = 3, max = 50)
        String manufacturer,

        @NotNull
        @Min(value = 0)
        BigDecimal price,

        @NotNull
        @Size(min = 3, max = 50)
        String category) {
}

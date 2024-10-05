package history.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

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

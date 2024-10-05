package customer.controller.payload;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateMedicationPayload(
        String description,
        String manufacturer,
        BigDecimal price,
        String category) {}

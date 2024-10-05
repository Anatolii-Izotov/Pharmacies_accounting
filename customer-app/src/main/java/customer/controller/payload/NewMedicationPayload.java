package customer.controller.payload;

import java.math.BigDecimal;

public record NewMedicationPayload(String name, String description, String manufacturer,
                                   BigDecimal price, String category) {
}

package customer.entity;

import java.math.BigDecimal;
import java.util.UUID;

public record Medication(String id, String name, String description,
                         String manufacturer, BigDecimal price,
                         String category) {}

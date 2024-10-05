package history.enity;

import java.math.BigDecimal;

public record Medication(String id, String name, String description,
                         String manufacturer, BigDecimal price,
                         String category) {}

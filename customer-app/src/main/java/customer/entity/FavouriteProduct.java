package customer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.rmi.server.UID;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteProduct {
    private UUID id;

    private String productId;
}

package mate.academy.bookstoreappspring.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemQuantityRequestDto {

    @Min(1)
    private Integer quantity;
}

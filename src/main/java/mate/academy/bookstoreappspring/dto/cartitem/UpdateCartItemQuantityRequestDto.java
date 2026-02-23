package mate.academy.bookstoreappspring.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemQuantityRequestDto {

    @Min(1)
    @NotNull
    private Integer quantity;
}

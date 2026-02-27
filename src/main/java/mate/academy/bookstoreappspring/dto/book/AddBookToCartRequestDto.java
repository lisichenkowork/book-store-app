package mate.academy.bookstoreappspring.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBookToCartRequestDto {

    @NotNull(message = "Book id is required")
    private Long bookId;
    @Min(1)
    private int quantity;
}

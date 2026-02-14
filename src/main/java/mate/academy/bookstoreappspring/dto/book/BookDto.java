package mate.academy.bookstoreappspring.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class BookDto {

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Author is required")
    private String author;
    @NotBlank(message = "ISBN is required")
    private String isbn;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Cover image is required")
    private String coverImage;
    @NotEmpty(message = "Categories are required")
    private Set<Long> categories;
}

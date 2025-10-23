package mate.academy.bookstoreappspring.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookCreateRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;
    @NotBlank
    private String description;
    @NotBlank
    private String coverImage;

    private boolean isDeleted = false;
}

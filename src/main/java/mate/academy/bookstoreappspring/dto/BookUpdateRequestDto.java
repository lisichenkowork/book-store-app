package mate.academy.bookstoreappspring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookUpdateRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
    @NotBlank
    private String description;
    @NotBlank
    private String coverImage;
    @NotNull
    @DecimalMin(value = "0.0")
    private Double price;
}
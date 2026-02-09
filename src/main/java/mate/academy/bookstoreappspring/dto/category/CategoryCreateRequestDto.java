package mate.academy.bookstoreappspring.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateRequestDto {

    @NotBlank(message = "Name of category is required")
    @Size(min = 1, max = 255)
    private String name;
    private String description;
}

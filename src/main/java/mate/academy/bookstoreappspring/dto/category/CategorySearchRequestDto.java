package mate.academy.bookstoreappspring.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategorySearchRequestDto {

    @NotBlank
    private String name;
}

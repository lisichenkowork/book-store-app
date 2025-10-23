package mate.academy.bookstoreappspring.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverImage;
    private BigDecimal price;
}

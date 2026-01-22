package mate.academy.bookstoreappspring.dto.book;

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
    private boolean isDeleted = false;
}

package mate.academy.bookstoreappspring.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class BookUpdateRequestDto {

    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverImage;
    private BigDecimal price;
    private Set<Long> categories;
}

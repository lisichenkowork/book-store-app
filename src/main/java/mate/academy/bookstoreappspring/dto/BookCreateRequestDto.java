package mate.academy.bookstoreappspring.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookCreateRequestDto {

    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private boolean isDeleted = false;
}

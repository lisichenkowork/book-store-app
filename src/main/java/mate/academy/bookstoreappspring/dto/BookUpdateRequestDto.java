package mate.academy.bookstoreappspring.dto;

import lombok.Data;

@Data
public class BookUpdateRequestDto {
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverImage;
    private Double price;
}
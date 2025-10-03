package mate.academy.bookstoreappspring.dto;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String coverImage;
    private Double price;
}
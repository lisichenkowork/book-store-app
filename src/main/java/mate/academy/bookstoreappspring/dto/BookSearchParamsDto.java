package mate.academy.bookstoreappspring.dto;

public record BookSearchParamsDto(
        String[] authors,
        String[] titles) {
}

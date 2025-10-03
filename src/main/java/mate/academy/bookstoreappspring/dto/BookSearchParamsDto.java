package mate.academy.bookstoreappspring.dto;

public record BookSearchParamsDto(
        String[] author,
        String[] title) {
}

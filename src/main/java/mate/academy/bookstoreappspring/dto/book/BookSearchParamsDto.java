package mate.academy.bookstoreappspring.dto.book;

public record BookSearchParamsDto(
        String[] authors,
        String[] titles) {
}

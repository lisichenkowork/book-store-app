package mate.academy.bookstoreappspring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.dto.BookCreateRequestDto;
import mate.academy.bookstoreappspring.dto.BookSearchParamsDto;
import mate.academy.bookstoreappspring.dto.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Books",
        description = "Endpoints for managing books: create, read, update, delete, and search operations"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "Returns a list of all books stored in the database. "
                    + "Each book is represented as a BookDto object."
    )
    public List<BookDto> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Retrieves a specific book by its unique identifier (ID). "
                    + "If the book with the given ID does not exist, an error is returned."
    )
    public BookDto getBookById(
            @PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @Operation(
            summary = "Create a new book",
            description = "Creates and saves a new book in the database using the provided data. "
                    + "All required fields must be valid and included in the request body."
    )
    public BookDto createBook(
            @Valid
            @RequestBody BookCreateRequestDto createBookRequestDto) {
        return bookService.createBook(createBookRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete book by ID",
            description = "Removes the book with the specified ID from the database. "
                    + "If the book does not exist, an error is returned. "
                    + "Returns HTTP 204 No Content on success."
    )
    public void delete(
            @PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing book",
            description = "Updates the details of an existing book identified by ID. "
                    + "The updated data must be provided in the request body as BookUpdateRequestDto."
    )
    public BookDto updateBook(
            @PathVariable Long id,
            @Valid
            @RequestBody BookUpdateRequestDto updateBookRequestDto) {
        return bookService.updateById(id, updateBookRequestDto);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search books by parameters",
            description = "Searches for books that match the specified filter parameters "
                    + "(such as author, title, etc.) provided in BookSearchParamsDto. "
                    + "Returns a list of matching BookDto objects."
    )
    public List<BookDto> search(
            BookSearchParamsDto bookSearchParams) {
        return bookService.search(bookSearchParams);
    }
}

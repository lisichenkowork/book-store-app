package mate.academy.bookstoreappspring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.book.BookDto;
import mate.academy.bookstoreappspring.dto.book.BookSearchParamsDto;
import mate.academy.bookstoreappspring.dto.book.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Books",
        description = "Endpoints for managing books: create, read, update, delete, search"
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
    public Page<BookDto> getAllBooks(Pageable pageable) {
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            summary = "Create a new book",
            description = "Creates and saves a new book in the database using the provided data. "
                    + "All required fields must be valid and included in the request body."
    )
    public BookDto createBook(
            @Valid
            @RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            summary = "Delete book by ID",
            description = "Removes the book with the specified ID from the database. "
                    + "If the book does not exist, an error is returned. "
                    + "Returns HTTP 204 No Content on success."
    )
    public void delete(
            @PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            summary = "Update existing book",
            description = "Updates the details of an existing book identified by ID. "
                    + "The data must be provided in the request body as BookUpdateRequestDto."
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
    public Page<BookDto> search(
            @RequestParam(required = false) String[] authors,
            @RequestParam(required = false) String[] titles,
            Pageable pageable) {
        BookSearchParamsDto params = new BookSearchParamsDto(authors, titles);
        return bookService.search(params, pageable);
    }

}

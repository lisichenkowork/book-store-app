package mate.academy.bookstoreappspring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.dto.BookCreateRequestDto;
import mate.academy.bookstoreappspring.dto.BookSearchParamsDto;
import mate.academy.bookstoreappspring.dto.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public BookDto createBook(@Valid @RequestBody BookCreateRequestDto createBookRequestDto) {
        return bookService.createBook(createBookRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequestDto updateBookRequestDto) {

        return bookService.updateById(id, updateBookRequestDto);
    }

    @GetMapping("/search")
    public List<BookDto> search(BookSearchParamsDto bookSearchParams){
        return bookService.search(bookSearchParams);
    }
}

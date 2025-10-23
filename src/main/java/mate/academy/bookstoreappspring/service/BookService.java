package mate.academy.bookstoreappspring.service;

import java.util.List;
import mate.academy.bookstoreappspring.dto.BookCreateRequestDto;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.dto.BookSearchParamsDto;
import mate.academy.bookstoreappspring.dto.BookUpdateRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto createBook(BookCreateRequestDto createBookRequestDto);

    BookDto findById(Long id);

    List<BookDto> getAllBooks(Pageable pageable);

    void deleteById(Long id);

    BookDto updateById(Long id, BookUpdateRequestDto updateBookRequestDto);

    List<BookDto> search(BookSearchParamsDto bookSearchParams, Pageable pageable);
}

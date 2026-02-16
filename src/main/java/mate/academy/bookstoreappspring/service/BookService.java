package mate.academy.bookstoreappspring.service;

import mate.academy.bookstoreappspring.dto.book.BookDto;
import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.book.BookSearchParamsDto;
import mate.academy.bookstoreappspring.dto.book.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto createBook(BookDto createBookRequestDto);

    BookDto findById(Long id);

    Page<BookDto> getAllBooks(Pageable pageable);

    void deleteById(Long id);

    BookDto updateById(Long id, BookUpdateRequestDto updateBookRequestDto);

    Page<BookDto> search(BookSearchParamsDto bookSearchParams, Pageable pageable);

    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);

    Book getEntityByIdOrThrow(Long id);

}

package mate.academy.bookstoreappspring.service;

import java.util.List;

import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.dto.CreateBookRequestDto;
import mate.academy.bookstoreappspring.dto.UpdateBookRequestDto;

public interface BookService {

    BookDto createBook(CreateBookRequestDto createBookRequestDto);

    BookDto findById(Long id);

    List<BookDto> getAllBooks();

    void deleteById(Long id);

    BookDto updateById(Long id, UpdateBookRequestDto updateBookRequestDto);
}

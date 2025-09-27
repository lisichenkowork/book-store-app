package mate.academy.bookstoreappspring.service;

import java.util.List;

import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.BookRepository;
import org.springframework.stereotype.Service;

public interface BookService {

    BookDto createBook(Book book);

    BookDto findById(Long id);

    List<BookDto> getAllBooks();
}

package mate.academy.bookstoreappspring.repository;

import java.util.List;
import java.util.Optional;

import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> getAll();

    Optional<Book> findById(Long id);
}

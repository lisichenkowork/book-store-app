package mate.academy.bookstoreappspring.repository;

import java.util.List;
import mate.academy.bookstoreappspring.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

}

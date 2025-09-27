package mate.academy.bookstoreappspring.service;

import java.util.List;
import mate.academy.bookstoreappspring.model.Book;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

}

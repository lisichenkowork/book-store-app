package mate.academy.bookstoreappspring.repository;

import mate.academy.bookstoreappspring.model.Book;

import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

}

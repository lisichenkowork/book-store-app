package mate.academy.bookstoreappspring.service;

import mate.academy.bookstoreappspring.model.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

}

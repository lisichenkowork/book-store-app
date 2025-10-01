package mate.academy.bookstoreappspring.repository;

import java.util.List;
import java.util.Optional;

import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}

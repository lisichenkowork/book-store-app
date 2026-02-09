package mate.academy.bookstoreappspring.repository.book;

import java.util.List;
import mate.academy.bookstoreappspring.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByCategories_id(Long categoriesId, Pageable pageable);

}

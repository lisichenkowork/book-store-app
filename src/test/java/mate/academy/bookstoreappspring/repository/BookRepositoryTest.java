package mate.academy.bookstoreappspring.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.model.Category;
import mate.academy.bookstoreappspring.repository.book.BookRepository;
import mate.academy.bookstoreappspring.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    @DisplayName("""
    Find all books by category id
    """)
    void findAllByCategoriesId_validCategoryId_returnsOneBook() {
        // given
        Category category = new Category();
        category.setName("Fiction");
        categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Book");
        book.setAuthor("Author");
        book.setIsbn("ISBN-123");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("Test");
        book.setCategories(Set.of(category));

        bookRepository.save(book);

        // when
        List<Book> actual = bookRepository.findAllByCategoriesId(
                category.getId(),
                PageRequest.of(0, 10)
        ).getContent();

        // then
        Assertions.assertEquals(1, actual.size());
    }
}

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


    private Category saveCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private Book saveBook(String title, String isbn, Category... categories) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("Author");
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.TEN);
        book.setDescription("Test");
        book.setCategories(Set.of(categories));
        return bookRepository.save(book);
    }

    @Test
    @DisplayName("""
    Find all books by category id
    """)
    void findAllByCategoriesId_validCategoryId_returnsOneBook() {
        // given
        Category category = saveCategory("Fiction");
        saveBook("Book", "ISBN-123", category);

        // when
        List<Book> actual = bookRepository.findAllByCategoriesId(
                category.getId(),
                PageRequest.of(0, 10)
        ).getContent();

        // then
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("""
    Find all books by a category id that has no books returns an empty page
    """)
    void findAllByCategoriesId_categoryWithoutBooks_returnsEmpty() {
        // given
        Category empty = saveCategory("Empty");
        Category other = saveCategory("Other");
        saveBook("Other book", "ISBN-OTHER", other);

        // when
        List<Book> actual = bookRepository.findAllByCategoriesId(
                empty.getId(),
                PageRequest.of(0, 10)
        ).getContent();

        // then
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("""
    Find all books by category id returns only books that belong to that category
    """)
    void findAllByCategoriesId_multipleCategories_returnsOnlyMatching() {
        // given
        Category fiction = saveCategory("Fiction");
        Category science = saveCategory("Science");
        Book fictionBook = saveBook("Dune", "ISBN-DUNE", fiction);
        saveBook("Cosmos", "ISBN-COSMOS", science);

        // when
        List<Book> actual = bookRepository.findAllByCategoriesId(
                fiction.getId(),
                PageRequest.of(0, 10)
        ).getContent();

        // then
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(fictionBook.getId(), actual.get(0).getId());
    }

    @Test
    @DisplayName("""
    Soft-deleted books are excluded from query results
    """)
    void findById_softDeletedBook_isNotReturned() {
        // given
        Book book = saveBook("To delete", "ISBN-DEL");

        // when
        bookRepository.delete(book);

        // then
        Assertions.assertTrue(bookRepository.findById(book.getId()).isEmpty());
    }
}

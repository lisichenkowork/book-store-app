package mate.academy.bookstoreappspring;

import java.math.BigDecimal;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreAppSpringApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreAppSpringApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("Java for beginners");
                book.setAuthor("<NAME>");
                book.setIsbn("1234567890");
                book.setPrice(BigDecimal.valueOf(100));
                book.setDescription("Java for beginners book");
                book.setCoverImage("url");

                bookService.save(book);

                System.out.println(bookService.findAll());
            }
        };
    }

}

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

}

package mate.academy.bookstoreappspring.repository.book.spec;

import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider {

    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb)
                -> root.get("author").in(Arrays.stream(params).toList());
    }
}

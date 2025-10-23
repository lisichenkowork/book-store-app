package mate.academy.bookstoreappspring.repository.book.spec;

import java.util.Arrays;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    private static final String AUTHOR_FIELD_NAME = "author";

    @Override
    public String getKey() {
        return AUTHOR_FIELD_NAME;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb)
                -> root.get(AUTHOR_FIELD_NAME).in(Arrays.stream(params).toList());
    }
}

package mate.academy.bookstoreappspring.repository.book.spec;

import java.util.Arrays;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    private static final String TITLE_FIELD_NAME = "title";

    @Override
    public String getKey() {
        return TITLE_FIELD_NAME;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb)
                -> root.get(TITLE_FIELD_NAME).in(Arrays.stream(params).toList());
    }
}

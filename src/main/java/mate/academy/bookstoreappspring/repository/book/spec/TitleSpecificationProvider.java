package mate.academy.bookstoreappspring.repository.book.spec;

import mate.academy.bookstoreappspring.dto.BookSearchParamsDto;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return "title";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb)
                -> root.get("title").in(Arrays.stream(params).toList());
    }
}

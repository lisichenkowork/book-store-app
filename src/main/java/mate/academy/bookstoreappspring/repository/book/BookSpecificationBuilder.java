package mate.academy.bookstoreappspring.repository.book;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.BookSearchParamsDto;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.repository.SpecificationBuilder;
import mate.academy.bookstoreappspring.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParamsDto bookSearchParams) {
        Specification<Book> spec = Specification.allOf();

        Map<String, String[]> map = new HashMap<>();

        map.put("title", bookSearchParams.titles());
        map.put("author", bookSearchParams.authors());

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            if (entry.getValue() != null && entry.getValue().length > 0) {
                spec = spec.and(specificationProviderManager
                        .getSpecificationProvider(entry.getKey())
                        .getSpecification(entry.getValue()));
            }
        }

        return spec;
    }
}

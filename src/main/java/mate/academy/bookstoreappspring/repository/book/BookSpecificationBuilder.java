package mate.academy.bookstoreappspring.repository.book;

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

        if(bookSearchParams.title() != null && bookSearchParams.title().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(bookSearchParams.title()));
        }

        if(bookSearchParams.author() != null && bookSearchParams.author().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(bookSearchParams.author()));
        }

        return spec;
    }
}

package mate.academy.bookstoreappspring.repository;

import mate.academy.bookstoreappspring.dto.BookSearchParamsDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {

    Specification<T> build(BookSearchParamsDto bookSearchParams);
}

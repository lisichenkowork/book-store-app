package mate.academy.bookstoreappspring.service.category;

import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.category.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto save(CategoryDto dto);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryDto dto);

    void deleteById(Long id);

    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);

}

package mate.academy.bookstoreappspring.service.category;

import java.util.List;
import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.category.CategoryCreateRequestDto;
import mate.academy.bookstoreappspring.dto.category.CategoryDto;
import mate.academy.bookstoreappspring.dto.category.CategoryUpdateRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto save(CategoryCreateRequestDto dto);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryUpdateRequestDto dto);

    void delete(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);

}

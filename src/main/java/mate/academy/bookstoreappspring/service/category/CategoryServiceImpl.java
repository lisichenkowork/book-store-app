package mate.academy.bookstoreappspring.service.category;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.category.CategoryDto;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.mapper.CategoryMapper;
import mate.academy.bookstoreappspring.model.Category;
import mate.academy.bookstoreappspring.repository.category.CategoryRepository;
import mate.academy.bookstoreappspring.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookService bookService;
    private final CategoryMapper mapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        Category entity = mapper.toEntity(dto);

        Category saved = categoryRepository.save(entity);

        return mapper.toDto(saved);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category categoryById = getByIdOrThrow(id);

        return mapper.toDto(categoryById);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category categoryById = getByIdOrThrow(id);

        mapper.updateEntityFromDto(dto, categoryById);

        Category saved = categoryRepository.save(categoryById);

        return mapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        Category categoryById = getByIdOrThrow(id);

        categoryRepository.delete(categoryById);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {
        existsByIdOrThrow(id);
        return bookService.getBooksByCategoryId(id, pageable);
    }

    private Category getByIdOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Category not found with id: " + id));
    }

    private void existsByIdOrThrow(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Category not found with id: " + id
            );
        }
    }
}

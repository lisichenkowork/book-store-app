package mate.academy.bookstoreappspring.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookstoreappspring.dto.book.BookDto;
import mate.academy.bookstoreappspring.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreappspring.dto.book.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(target = "categories", ignore = true)
    Book toEntity(CreateBookRequestDto dto);

    @Mapping(target = "categories", ignore = true)
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategoryIds(Book entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBook(BookUpdateRequestDto dto, @MappingTarget Book entity);

    @AfterMapping
    default void updateCategories(@MappingTarget Book entity, BookUpdateRequestDto dto) {

        if (dto.getCategories() == null) {
            return;
        }
        Set<Category> categories = dto.getCategories().stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());

        entity.setCategories(categories);

    }

    @AfterMapping
    default void mapCategoriesToIds(@MappingTarget BookDto dto, Book entity) {

        Set<Long> ids = (entity.getCategories() != null)
                ? entity.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet())
                : Set.of();

        dto.setCategories(ids);
    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget Book entity, CreateBookRequestDto dto) {
        Set<Category> categories = dto.getCategories().stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());

        entity.setCategories(categories);
    }
}

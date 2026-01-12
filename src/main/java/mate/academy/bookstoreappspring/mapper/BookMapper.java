package mate.academy.bookstoreappspring.mapper;

import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.book.BookCreateRequestDto;
import mate.academy.bookstoreappspring.dto.book.BookDto;
import mate.academy.bookstoreappspring.dto.book.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(BookCreateRequestDto createBookRequestDto);

    void updateModelFromDto(BookUpdateRequestDto dto, @MappingTarget Book entity);
}

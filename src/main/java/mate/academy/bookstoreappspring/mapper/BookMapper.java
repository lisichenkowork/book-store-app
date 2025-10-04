package mate.academy.bookstoreappspring.mapper;

import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.dto.BookUpdateRequestDto;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.dto.BookCreateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    BookDto toDto(BookCreateRequestDto createBookRequestDto);

    Book toModel(BookCreateRequestDto createBookRequestDto);

    void updateModelFromDto(BookUpdateRequestDto dto, @MappingTarget Book entity);

    List<BookDto> toDtoList(List<Book> books);
}

package mate.academy.bookstoreappspring.mapper;

import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.dto.UpdateBookRequestDto;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.dto.CreateBookRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    BookDto toDto(CreateBookRequestDto createBookRequestDto);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateModelFromDto(UpdateBookRequestDto dto, @MappingTarget Book entity);
}

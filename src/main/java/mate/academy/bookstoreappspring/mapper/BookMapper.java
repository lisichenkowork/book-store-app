package mate.academy.bookstoreappspring.mapper;

import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.BookDto;
import mate.academy.bookstoreappspring.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto (Book book);
}

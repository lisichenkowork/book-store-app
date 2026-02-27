package mate.academy.bookstoreappspring.mapper;

import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.cartitem.CartItemDto;
import mate.academy.bookstoreappspring.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem entity);

}

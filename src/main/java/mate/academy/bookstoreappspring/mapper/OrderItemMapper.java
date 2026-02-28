package mate.academy.bookstoreappspring.mapper;

import java.util.Set;
import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreappspring.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    Set<OrderItemDto> toDtoSet(Set<OrderItem> orderItem);
}

package mate.academy.bookstoreappspring.mapper;

import mate.academy.bookstoreappspring.config.MapperConfig;
import mate.academy.bookstoreappspring.dto.order.OrderDto;
import mate.academy.bookstoreappspring.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);
}

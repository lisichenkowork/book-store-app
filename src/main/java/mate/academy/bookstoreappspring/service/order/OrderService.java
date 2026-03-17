package mate.academy.bookstoreappspring.service.order;

import java.util.Set;
import mate.academy.bookstoreappspring.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreappspring.dto.order.OrderDto;
import mate.academy.bookstoreappspring.dto.order.UpdateStatusRequestDto;
import mate.academy.bookstoreappspring.dto.orderitem.OrderItemDto;

public interface OrderService {

    Set<OrderDto> findOrdersForCurrentUser();

    OrderDto placeOrder(CreateOrderRequestDto dto);

    Set<OrderItemDto> getOrderItem(Long orderId);

    OrderItemDto getConcreteOrderItem(Long orderId, Long orderItemId);

    OrderDto updateStatusForOrder(Long orderId, UpdateStatusRequestDto dto);
}

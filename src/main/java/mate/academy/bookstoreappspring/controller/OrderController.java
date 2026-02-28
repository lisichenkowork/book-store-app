package mate.academy.bookstoreappspring.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreappspring.dto.order.OrderDto;
import mate.academy.bookstoreappspring.dto.order.UpdateStatusRequestDto;
import mate.academy.bookstoreappspring.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreappspring.model.Order;
import mate.academy.bookstoreappspring.model.OrderItem;
import mate.academy.bookstoreappspring.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public Set<OrderDto> getOrders() {
        return orderService.findOrdersForCurrentUser();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public OrderDto placeOrder(@RequestBody CreateOrderRequestDto dto) {
        return orderService.placeOrder(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public Set<OrderItemDto> getOrderItem(@PathVariable Long orderId) {
        return orderService.getOrderItem(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getConcreteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getConcreteOrderItem(orderId, itemId);
    }

    @PatchMapping("/{id}")
    public OrderDto updateStatusForOrder(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequestDto dto) {

        return orderService.updateStatusForOrder(id, dto);
    }

}

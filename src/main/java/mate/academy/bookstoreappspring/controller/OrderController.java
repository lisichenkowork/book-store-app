package mate.academy.bookstoreappspring.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.order.OrderDto;
import mate.academy.bookstoreappspring.service.order.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}

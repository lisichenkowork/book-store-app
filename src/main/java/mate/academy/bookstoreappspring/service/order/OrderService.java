package mate.academy.bookstoreappspring.service.order;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.order.OrderDto;
import mate.academy.bookstoreappspring.mapper.OrderMapper;
import mate.academy.bookstoreappspring.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderDto getOrderById(Long id) {
        return orderMapper.toDto(orderRepository.getOrderById(id));
    }
}

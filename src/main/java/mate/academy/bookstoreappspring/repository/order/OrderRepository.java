package mate.academy.bookstoreappspring.repository.order;

import mate.academy.bookstoreappspring.dto.order.OrderDto;
import mate.academy.bookstoreappspring.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order getOrderById(Long id);
}

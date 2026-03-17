package mate.academy.bookstoreappspring.repository.orderitem;

import mate.academy.bookstoreappspring.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

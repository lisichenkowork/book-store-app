package mate.academy.bookstoreappspring.repository.order;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreappspring.model.Order;
import mate.academy.bookstoreappspring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> getOrderById(Long id);

    Set<Order> findAllByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}

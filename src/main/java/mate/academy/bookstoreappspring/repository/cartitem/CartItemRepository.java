package mate.academy.bookstoreappspring.repository.cartitem;

import java.util.Optional;
import mate.academy.bookstoreappspring.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> getCartItemById(Long id);
}

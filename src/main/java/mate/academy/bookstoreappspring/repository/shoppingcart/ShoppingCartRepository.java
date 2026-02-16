package mate.academy.bookstoreappspring.repository.shoppingcart;

import java.util.Optional;
import mate.academy.bookstoreappspring.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> getShoppingCartByUserId(Long userId);
}

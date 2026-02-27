package mate.academy.bookstoreappspring.service.cartitem;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.model.CartItem;
import mate.academy.bookstoreappspring.model.ShoppingCart;
import mate.academy.bookstoreappspring.repository.cartitem.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem createCartItem(ShoppingCart shoppingCart, Book book, Integer quantity) {

        Optional<CartItem> existItem = shoppingCart.getCartItems()
                .stream()
                .filter(c -> c.getBook().getId().equals(book.getId()))
                .findFirst();

        if (existItem.isPresent()) {
            //update quantity if exist
            CartItem cartItem = existItem.get();
            return updateCartItem(cartItem, quantity);
        } else {
            //create new if not
            CartItem cartItem = new CartItem(shoppingCart, book, quantity);
            shoppingCart.getCartItems().add(cartItem);
            return cartItemRepository.save(cartItem);
        }
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem, Integer quantity) {
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem findCartItemByIdAndShoppingCartId(Long id, Long shoppingCartId) {
        return cartItemRepository.getCartItemByIdAndShoppingCartId(id, shoppingCartId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cart Item not found with id:" + id));
    }

    @Override
    public void deleteCartItem(Long cartItemId, Long shoppingCartId) {
        //checks if cartItem exist before deleting
        CartItem cartItem = findCartItemByIdAndShoppingCartId(cartItemId, shoppingCartId);

        cartItemRepository.delete(cartItem);
    }
}

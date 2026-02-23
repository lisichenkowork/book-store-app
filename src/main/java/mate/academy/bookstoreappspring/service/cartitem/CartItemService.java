package mate.academy.bookstoreappspring.service.cartitem;

import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.model.CartItem;
import mate.academy.bookstoreappspring.model.ShoppingCart;

public interface CartItemService {

    CartItem createCartItem(ShoppingCart shoppingCart, Book book, Integer quantity);

    CartItem updateCartItem(CartItem cartItem, Integer quantity);

    CartItem findCartItemByIdAndShoppingCartId(Long id, Long userId);

    void deleteCartItem(Long cartItemId, Long shoppingCartId);

}

package mate.academy.bookstoreappspring.service.cartitem;

import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.model.CartItem;
import mate.academy.bookstoreappspring.model.ShoppingCart;

public interface CartItemService {

    CartItem createCartItem(ShoppingCart shoppingCart, Book book, Integer quantity);

    CartItem updateCartItem(CartItem cartItem, Integer quantity);

    CartItem getCartItemOrThrow(Long id);

    void deleteCartItem(Long cartItemId);

}

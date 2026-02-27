package mate.academy.bookstoreappspring.service.shoppingcart;

import mate.academy.bookstoreappspring.dto.book.AddBookToCartRequestDto;
import mate.academy.bookstoreappspring.dto.cartitem.CartItemDto;
import mate.academy.bookstoreappspring.dto.cartitem.UpdateCartItemQuantityRequestDto;
import mate.academy.bookstoreappspring.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart();

    CartItemDto addCartItemToShoppingCart(AddBookToCartRequestDto dto);

    CartItemDto updateCartItemQuantity(Long cartItemId, UpdateCartItemQuantityRequestDto dto);

    void deleteCartItem(Long cartItemId);
}

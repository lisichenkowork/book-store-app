package mate.academy.bookstoreappspring.service.shoppingcart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.book.AddBookToCartRequestDto;
import mate.academy.bookstoreappspring.dto.cartitem.CartItemDto;
import mate.academy.bookstoreappspring.dto.cartitem.UpdateCartItemQuantityRequestDto;
import mate.academy.bookstoreappspring.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstoreappspring.mapper.CartItemMapper;
import mate.academy.bookstoreappspring.mapper.ShoppingCartMapper;
import mate.academy.bookstoreappspring.model.Book;
import mate.academy.bookstoreappspring.model.CartItem;
import mate.academy.bookstoreappspring.model.ShoppingCart;
import mate.academy.bookstoreappspring.model.User;
import mate.academy.bookstoreappspring.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstoreappspring.service.BookService;
import mate.academy.bookstoreappspring.service.cartitem.CartItemService;
import mate.academy.bookstoreappspring.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookService bookService;
    private final CartItemMapper cartItemMapper;
    private final CartItemService cartItemService;

    @Transactional
    @Override
    public ShoppingCartDto getShoppingCart() {
        User loggedUser = getLoggedUser();

        ShoppingCart shoppingCart = getOrCreateShoppingCart(loggedUser);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public CartItemDto addCartItemToShoppingCart(AddBookToCartRequestDto dto) {
        User loggedUser = getLoggedUser();

        // Get shopping cart or create a new one if the user doesn't have one yet
        ShoppingCart shoppingCart = getOrCreateShoppingCart(loggedUser);

        Book bookById = bookService.getEntityByIdOrThrow(dto.getBookId());

        CartItem addedCartItem = cartItemService
                .createCartItem(shoppingCart, bookById, dto.getQuantity());

        return cartItemMapper.toDto(addedCartItem);
    }

    @Override
    public CartItemDto updateCartItemQuantity(
            Long cartItemId,
            UpdateCartItemQuantityRequestDto dto) {
        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be 0 or less than 1, quantity is: "
                            + dto.getQuantity());
        }

        ShoppingCart shoppingCartEntity = getShoppingCartEntityForLoggedUser();

        CartItem cartItemById = cartItemService
                .findCartItemByIdAndShoppingCartId(cartItemId, shoppingCartEntity.getId());

        CartItem updated = cartItemService.updateCartItem(cartItemById, dto.getQuantity());

        return cartItemMapper.toDto(updated);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        ShoppingCart shoppingCartEntity = getShoppingCartEntityForLoggedUser();
        cartItemService.deleteCartItem(cartItemId, shoppingCartEntity.getId());
    }

    private ShoppingCart getOrCreateShoppingCart(User user) {
        return shoppingCartRepository.getShoppingCartByUserId(user.getId())
                .orElseGet(() -> createShoppingCart(user));
    }

    private ShoppingCart createShoppingCart(User loggedUser) {
        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.setUser(loggedUser);
        return shoppingCartRepository.save(shoppingCart);
    }

    private User getLoggedUser() {
        return userService.getLoggedUser();
    }

    private ShoppingCart getShoppingCartEntityForLoggedUser() {
        User loggedUser = getLoggedUser();

        return getOrCreateShoppingCart(loggedUser);
    }

}


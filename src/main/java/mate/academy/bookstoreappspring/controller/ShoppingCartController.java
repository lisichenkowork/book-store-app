package mate.academy.bookstoreappspring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.book.AddBookToCartRequestDto;
import mate.academy.bookstoreappspring.dto.cartitem.CartItemDto;
import mate.academy.bookstoreappspring.dto.cartitem.UpdateCartItemQuantityRequestDto;
import mate.academy.bookstoreappspring.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstoreappspring.service.shoppingcart.ShoppingCartService;
import mate.academy.bookstoreappspring.service.shoppingcart.ShoppingCartServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart for logged-in user",
            description = "Returns the shopping cart with all items for the authenticated user")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to shopping cart",
            description = "Adds a book to the authenticated user's shopping cart "
                    + "or updates quantity if it already exists")
    public CartItemDto createCartItem(
            @RequestBody @Valid AddBookToCartRequestDto dto) {
        return shoppingCartService.addCartItemToShoppingCart(dto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity of cart item",
            description = "Updates the quantity of a specific cart item "
                    + "in the authenticated user's shopping cart")
    public CartItemDto updateCartItemQuantity(
            @Parameter(description = "ID of the cart item to update") @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemQuantityRequestDto dto) {
        return shoppingCartService.updateCartItemQuantity(cartItemId, dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete cart item",
            description = "Removes a specific item from "
                    + "the authenticated user's shopping cart")
    public void deleteCartItem(
            @Parameter(
                    description = "ID of the cart item to delete")
            @PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}

package mate.academy.bookstoreappspring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Min(1)
    @NotNull
    @Column(nullable = false)
    private Integer quantity;

    public CartItem(ShoppingCart shoppingCart, Book book, Integer quantity) {
        if (shoppingCart == null) {
            throw new IllegalArgumentException("Shopping cart cannot be null");
        }

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be less or equal 0: quantity=" + quantity);
        }

        this.shoppingCart = shoppingCart;
        this.book = book;
        this.quantity = quantity;
    }
}

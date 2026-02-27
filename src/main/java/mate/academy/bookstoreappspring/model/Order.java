package mate.academy.bookstoreappspring.model;

import com.fasterxml.jackson.core.FormatFeature;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Lazy;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @NotNull
    private BigDecimal total;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private String shippingAddress;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

}

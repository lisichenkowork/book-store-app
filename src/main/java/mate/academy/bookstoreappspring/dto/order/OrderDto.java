package mate.academy.bookstoreappspring.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import mate.academy.bookstoreappspring.dto.orderitem.OrderItemDto;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Set<OrderItemDto> orderItems;
}

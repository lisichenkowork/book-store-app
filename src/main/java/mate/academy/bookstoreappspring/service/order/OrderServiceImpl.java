package mate.academy.bookstoreappspring.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreappspring.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreappspring.dto.order.OrderDto;
import mate.academy.bookstoreappspring.dto.order.UpdateStatusRequestDto;
import mate.academy.bookstoreappspring.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreappspring.exception.AccessDeniedException;
import mate.academy.bookstoreappspring.exception.EmptyCartException;
import mate.academy.bookstoreappspring.exception.EntityNotFoundException;
import mate.academy.bookstoreappspring.mapper.OrderItemMapper;
import mate.academy.bookstoreappspring.mapper.OrderMapper;
import mate.academy.bookstoreappspring.model.CartItem;
import mate.academy.bookstoreappspring.model.Order;
import mate.academy.bookstoreappspring.model.OrderItem;
import mate.academy.bookstoreappspring.model.ShoppingCart;
import mate.academy.bookstoreappspring.model.Status;
import mate.academy.bookstoreappspring.model.User;
import mate.academy.bookstoreappspring.repository.order.OrderRepository;
import mate.academy.bookstoreappspring.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstoreappspring.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Set<OrderDto> findOrdersForCurrentUser() {
        User loggedUser = userService.getLoggedUser();

        Set<Order> allByUserId = orderRepository.findAllByUserId(loggedUser.getId());

        return orderMapper.toDtoSet(allByUserId);
    }

    @Override
    @Transactional
    public OrderDto placeOrder(CreateOrderRequestDto dto) {
        User loggedUser = userService.getLoggedUser();

        ShoppingCart shoppingCart = getValidShoppingCart(loggedUser);

        Order order = createOrder(loggedUser, dto.getShippingAddress(), shoppingCart.getCartItems());

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemDto> getOrderItem(Long orderId) {
        User loggedUser = userService.getLoggedUser();

        Order orderById = orderRepository.findByIdAndUserId(orderId, loggedUser.getId()).orElseThrow(
                () -> new AccessDeniedException("You can access only your own order"));

        Set<OrderItem> orderItems = orderById.getOrderItems();

        return orderItemMapper.toDtoSet(orderItems);

    }

    @Override
    public OrderItemDto getConcreteOrderItem(Long orderId, Long orderItemId) {
        return getOrderItem(orderId)
                .stream()
                .filter(o -> o.getId().equals(orderItemId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + orderItemId));
    }

    @Override
    @Transactional
    public OrderDto updateStatusForOrder(Long orderId, UpdateStatusRequestDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));

        Status newStatus;
        try {
            newStatus = Status.valueOf(dto.getStatus());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + dto.getStatus());
        }

        order.setStatus(newStatus);

        return orderMapper.toDto(order);
    }

    private ShoppingCart getValidShoppingCart(User user) {
        ShoppingCart cart = shoppingCartRepository
                .getShoppingCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart for user with id: " + user.getId()));

        if (cart.getCartItems().isEmpty()) {
            throw new EmptyCartException("Shopping cart is empty");
        }
        return cart;
    }

    private Order createOrder(User user, String shippingAddress, Set<CartItem> cartItems) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setShippingAddress(shippingAddress);

        Set<OrderItem> orderItemsSet = cartItems.stream()
                .map(cartItem -> mapCartItemToOrderItem(order, cartItem))
                .collect(Collectors.toSet());

        order.setOrderItems(orderItemsSet);

        order.setTotal(calculateTotal(orderItemsSet));

        return order;
    }

    private OrderItem mapCartItemToOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()))); // ціна з quantity
        return orderItem;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

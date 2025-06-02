package com.predators.service;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.dto.order.OrderMapper;
import com.predators.dto.order.OrderRequestDto;
import com.predators.entity.*;
import com.predators.entity.enums.OrderStatus;
import com.predators.exception.ImpossibleChangeCurrentOrderStatusException;
import com.predators.exception.OrderNotFoundException;
import com.predators.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ProductService productService;

    private final ShopUserService shopUserService;

    private final CartService cartService;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public Order create(OrderRequestDto dto) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        Order order = orderMapper.toEntity(dto);
        Cart cart = currentUser.getCart();

        List<OrderItem> items = new ArrayList<>();
        Set<CartItem> cartItems = cart.getCartItems();

        Map<Long, CartItem> map = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            map.put(cartItem.getProduct().getId(), cartItem);
        }

        for (ProductToItemDto productDto : dto.items()) {
            Product product = productService.getById(productDto.productId());
            CartItem cartItem = map.get(productDto.productId());
            if (cartItem == null) {
                continue;
            }
            int quantityOrderItem = Math.min(cartItem.getQuantity(), productDto.quantity());
            OrderItem orderItem = getOrderItem(productDto, product);
            items.add(orderItem);
            if (cartItem.getQuantity() <= quantityOrderItem) {
                cart.getCartItems().remove(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() - quantityOrderItem);
            }
        }

        order.setOrderItems(items);
        Order entity = orderRepository.save(order);
        log.debug("Created order: " + entity);
        cartService.save(cart);
        return entity;
    }

    private OrderItem getOrderItem(ProductToItemDto productDto, Product product) {
        return OrderItem.builder()
                .product(product)
                .quantity(productDto.quantity())
                .priceAtPurchase(product.getPrice()
                        .subtract(product.getDiscountPrice() == null ?
                                BigDecimal.ZERO : product.getDiscountPrice())
                        .multiply(BigDecimal.valueOf(productDto.quantity())))
                .build();
    }

    @Override
    public void delete(Long id) {
        Order order = getById(id);
        orderRepository.deleteById(order.getId());
    }

    @Override
    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = getById(id);
        order.setStatus(newStatus);
        order.setUpdatedAt(Timestamp.from(Instant.now()));
        return orderRepository.save(order);
    }

    @Override
    public String getStatus(Long id) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        return orderRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(()-> new OrderNotFoundException("Order with id " + id + " not found"))
                .getStatus().toString();
    }

    @Override
    public List<Order> getAllByStatus(OrderStatus status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public List<Order> getHistory() {
        ShopUser currentUser = shopUserService.getCurrentUser();
        return orderRepository.findAllByUser_Id(currentUser.getId());
    }

    @Override
    public List<Order> getAllByStatusAndAfterDate(OrderStatus status, Timestamp afterDate) {
        return orderRepository.findAllByStatusAndAfterDate(status, afterDate);
    }

    @Override
    public Order cancel(Long id) {
        Order order = getById(id);
        if (order.getStatus().equals(OrderStatus.CREATED) || order.getStatus().equals(OrderStatus.PENDING)) {
            updateStatus(id, OrderStatus.CANCELLED);
        } else {
            throw new ImpossibleChangeCurrentOrderStatusException("You can't change status: " + order.getStatus());
        }
        return order;
    }


}

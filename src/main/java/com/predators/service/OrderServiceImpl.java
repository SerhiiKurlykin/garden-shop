package com.predators.service;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.dto.converter.OrderConverter;
import com.predators.dto.order.OrderRequestDto;
import com.predators.entity.Order;
import com.predators.entity.OrderItem;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.entity.enums.OrderStatus;
import com.predators.exception.OrderNotFoundException;
import com.predators.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderConverter orderConverter;

    private final ProductService productService;

    private final ShopUserService shopUserService;

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
        Order order = orderConverter.toEntity(dto);

        List<OrderItem> items = new ArrayList<>();
        for (ProductToItemDto productDto : dto.items()) {
            Product product = productService.getById(productDto.productId());
            BigDecimal discount = product.getDiscountPrice() == null ? BigDecimal.ZERO : product.getDiscountPrice();
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(productDto.quantity())
                    .priceAtPurchase(product.getPrice()
                            .subtract(discount)
                            .multiply(BigDecimal.valueOf(productDto.quantity())))
                    .build();
            items.add(orderItem);
        }

        order.setOrderItems(items);
        Order entity = orderRepository.save(order);
        log.debug("Created order: " + entity);

        return entity;
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
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
        return "";
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


}

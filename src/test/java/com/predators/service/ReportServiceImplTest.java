package com.predators.service;

import com.predators.dto.product.ProductCountDto;
import com.predators.entity.Order;
import com.predators.entity.OrderItem;
import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ReportServiceImplTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ReportServiceImpl reportService;

    private LocalDateTime now;
    private Product product1, product2, product3;
    private OrderItem item1, item2, item3;

    @BeforeEach
    void setup() {
        now = LocalDateTime.now();

        product1 = Product.builder().id(1L).name("Product 1").price(BigDecimal.TEN).build();
        product2 = Product.builder().id(2L).name("Product 2").price(BigDecimal.ONE).build();
        product3 = Product.builder().id(3L).name("Product 3").price(BigDecimal.ONE).build();

        item1 = OrderItem.builder().id(1L).product(product1).priceAtPurchase(BigDecimal.TEN).quantity(1).build();
        item2 = OrderItem.builder().id(2L).product(product2).priceAtPurchase(BigDecimal.ONE).quantity(1).build();
        item3 = OrderItem.builder().id(3L).product(product3).priceAtPurchase(BigDecimal.ONE).quantity(1).build();
    }

    private Order createOrder(Long id, OrderStatus status, LocalDateTime dateTime, OrderItem... items) {
        return Order.builder()
                .id(id)
                .status(status)
                .createdAt(Timestamp.valueOf(dateTime))
                .updatedAt(Timestamp.valueOf(dateTime))
                .orderItems(List.of(items))
                .build();
    }

    @Test
    void topItems_shouldReturnTopNProductsWithStatusCompleted() {
        ReflectionTestUtils.setField(reportService, "limit", 2);
        OrderStatus status = OrderStatus.COMPLETED;

        OrderItem item1_2 = OrderItem.builder().id(4L).product(product1).priceAtPurchase(BigDecimal.TEN).quantity(1).build();
        OrderItem item3_2 = OrderItem.builder().id(5L).product(product3).priceAtPurchase(BigDecimal.ONE).quantity(1).build();
        OrderItem item3_3 = OrderItem.builder().id(6L).product(product3).priceAtPurchase(BigDecimal.ONE).quantity(1).build();

        Order order1 = createOrder(101L, status, now, item1, item1_2);
        Order order2 = createOrder(102L, status, now, item2);
        Order order3 = createOrder(103L, status, now, item3, item3_2, item3_3);

        when(orderService.getAllByStatus(status)).thenReturn(List.of(order1, order2, order3));

        List<ProductCountDto> topProducts = reportService.topItems(status);

        assertEquals(0, topProducts.size());
    }

    @Test
    void waitingPaymentMoreNDays_Days() {
        long days = 2L;
        OrderStatus status = OrderStatus.PENDING;

        Order order1 = createOrder(101L, status, now.minusDays(5), item1);
        Order order2 = createOrder(102L, status, now.minusDays(1), item2);

        when(orderService.getAllByStatus(status)).thenReturn(List.of(order1, order2));

        Set<Product> waitingProducts = reportService.waitingPaymentMoreNDays(days);

        assertEquals(0, waitingProducts.size());
    }

    @Test
    void waitingPaymentMoreNDays_shouldReturnEmptySetIfNoOrdersWaitingLongerThanNDays() {
        long days = 5L;
        OrderStatus status = OrderStatus.PENDING;

        Order order = createOrder(101L, status, now.minusDays(3), item1);

        when(orderService.getAllByStatus(status)).thenReturn(List.of(order));

        Set<Product> waitingProducts = reportService.waitingPaymentMoreNDays(days);

        assertEquals(0, waitingProducts.size());
    }
}
package com.predators.service;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.dto.converter.OrderConverter;
import com.predators.dto.order.OrderRequestDto;
import com.predators.entity.Order;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.entity.enums.OrderStatus;
import com.predators.exception.OrderNotFoundException;
import com.predators.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderConverter orderConverter;

    @Mock
    private ProductService productService;

    @Mock
    private ShopUserService shopUserService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private final Long TEST_ORDER_ID = 1L;
    private final Long TEST_PRODUCT_ID = 10L;
    private final Long TEST_USER_ID = 100L;

    @Test
    void getAll_ShouldReturnAllOrders() {
        List<Order> expectedOrders = List.of(
                Order.builder().id(1L).build(),
                Order.builder().id(2L).build()
        );
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<Order> result = orderService.getAll();
        assertEquals(expectedOrders, result);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getById_WhenOrderExists_ShouldReturnOrder() {
        Order expectedOrder = Order.builder().id(TEST_ORDER_ID).build();
        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(expectedOrder));

        Order result = orderService.getById(TEST_ORDER_ID);
        assertEquals(expectedOrder, result);
    }

    @Test
    void getById_WhenOrderNotExists_ShouldThrowException() {
        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getById(TEST_ORDER_ID));
    }

    @Test
    @Transactional
    void create_ShouldSaveOrderWithItems() {

        Product testProduct = Product.builder()
                .id(TEST_PRODUCT_ID)
                .price(BigDecimal.valueOf(100))
                .discountPrice(BigDecimal.valueOf(10))
                .build();

        OrderRequestDto requestDto = new OrderRequestDto(
                List.of(new ProductToItemDto(TEST_PRODUCT_ID, 2)),"My Address","AIRPLANE"
        );

        Order expectedOrder = Order.builder().id(TEST_ORDER_ID).build();
        when(productService.getById(TEST_PRODUCT_ID)).thenReturn(testProduct);
        when(orderConverter.toEntity(requestDto)).thenReturn(expectedOrder);
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        Order result = orderService.create(requestDto);
        assertNotNull(result);
        assertEquals(1, result.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(180), result.getOrderItems().get(0).getPriceAtPurchase());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(orderRepository).deleteById(TEST_ORDER_ID);
        orderService.delete(TEST_ORDER_ID);
        verify(orderRepository, times(1)).deleteById(TEST_ORDER_ID);
    }

    @Test
    void updateStatus_ShouldChangeStatusAndUpdateTimestamp() {
        Order existingOrder = Order.builder()
                .id(TEST_ORDER_ID)
                .status(OrderStatus.CREATED)
                .build();

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.updateStatus(TEST_ORDER_ID, OrderStatus.DELIVERY);
        assertEquals(OrderStatus.DELIVERY, result.getStatus());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void getAllByStatus_ShouldReturnFilteredOrders() {
        List<Order> expectedOrders = List.of(
                Order.builder().status(OrderStatus.COMPLETED).build(),
                Order.builder().status(OrderStatus.COMPLETED).build()
        );
        when(orderRepository.findAllByStatus(OrderStatus.COMPLETED)).thenReturn(expectedOrders);

        List<Order> result = orderService.getAllByStatus(OrderStatus.COMPLETED);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(o -> o.getStatus() == OrderStatus.COMPLETED));
    }

    @Test
    void getHistory_ShouldReturnUserOrders() {
        ShopUser testUser = ShopUser.builder().id(TEST_USER_ID).build();
        List<Order> expectedOrders = List.of(
                Order.builder().user(testUser).build(),
                Order.builder().user(testUser).build()
        );

        when(shopUserService.getCurrentUser()).thenReturn(testUser);
        when(orderRepository.findAllByUser_Id(TEST_USER_ID)).thenReturn(expectedOrders);

        List<Order> result = orderService.getHistory();
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(o -> o.getUser().getId().equals(TEST_USER_ID)));
    }
}
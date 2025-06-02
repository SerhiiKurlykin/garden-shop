package com.predators.service;

import com.predators.entity.OrderItem;
import com.predators.repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Автоматически активирует Mockito
class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    void create_ShouldReturnSavedOrderItem() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        OrderItem result = orderItemService.create(orderItem);

        assertThat(result).isEqualTo(orderItem);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void saveAll_ShouldReturnSavedOrderItems() {
        List<OrderItem> orderItems = List.of(new OrderItem(), new OrderItem());
        when(orderItemRepository.saveAll(orderItems)).thenReturn(orderItems);

        List<OrderItem> result = orderItemService.saveAll(orderItems);

        assertThat(result).isEqualTo(orderItems);
        verify(orderItemRepository, times(1)).saveAll(orderItems);
    }
}
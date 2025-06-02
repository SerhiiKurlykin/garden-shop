package com.predators.controller.unitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predators.controller.OrderController;
import com.predators.dto.order.OrderMapper;
import com.predators.dto.order.OrderRequestDto;
import com.predators.dto.order.OrderResponseDto;
import com.predators.dto.orderitem.OrderItemMapper;
import com.predators.dto.orderitem.OrderItemResponseDto;
import com.predators.entity.Order;
import com.predators.entity.enums.DeliveryMethod;
import com.predators.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAll_ShouldReturnListOfOrders() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        Order order = new Order();
        OrderResponseDto dto = buildOrderResponseDto();
        when(orderService.getAll()).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(dto);
        mockMvc.perform(get("/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.id()))
                .andExpect(jsonPath("$[0].status").value(dto.status()));
    }

    @Test
    void getStatus_ShouldReturnOrdersByStatus() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        when(orderService.getAllByStatus(any())).thenReturn(List.of(new Order()));
        mockMvc.perform(get("/v1/orders/get-status/CREATED"))
                .andExpect(status().isOk());
    }

    @Test
    void create_ShouldCreateOrder() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        OrderRequestDto requestDto = new OrderRequestDto(List.of(), "Test Address", "Courier");
        Order order = new Order();
        OrderResponseDto responseDto = buildOrderResponseDto();
        when(orderService.create(any())).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(responseDto);
        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.id()));
    }

    @Test
    void getById_ShouldReturnOrderById() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        Order order = new Order();
        OrderResponseDto dto = buildOrderResponseDto();
        when(orderService.getById(anyLong())).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(dto);

        mockMvc.perform(get("/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.id()));
    }

    @Test
    void updateStatus_ShouldUpdateOrderStatus() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        Order updatedOrder = new Order();
        OrderResponseDto updatedDto = buildOrderResponseDto();
        when(orderService.updateStatus(anyLong(), any())).thenReturn(updatedOrder);
        when(orderMapper.toDto(updatedOrder)).thenReturn(updatedDto);

        mockMvc.perform(put("/v1/orders/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDto.id()));
    }

    @Test
    void getStatusById_ShouldReturnOrderStatus() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        when(orderService.getStatus(anyLong())).thenReturn("CREATED");
        mockMvc.perform(get("/v1/orders/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("CREATED"));
    }

    @Test
    void delete_ShouldDeleteOrder() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        doNothing().when(orderService).delete(anyLong());
        mockMvc.perform(delete("/v1/orders/1"))
                .andExpect(status().isOk());
    }

    private OrderResponseDto buildOrderResponseDto() {
        return OrderResponseDto.builder()
                .id(1L)
                .userId(10L)
                .status("NEW")
                .contactPhone("123456789")
                .deliveryAddress("Test Address")
                .deliveryMethod(DeliveryMethod.COURIER)
                .items(Collections.emptyList())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    private OrderItemResponseDto buildOrderItemResponseDto() {
        return OrderItemResponseDto.builder()
                .id(1L)
                .productId(1L)
                .quantity(2)
                .priceAtPurchase(BigDecimal.valueOf(40))
                .build();
    }
}

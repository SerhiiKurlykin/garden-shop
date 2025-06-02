package com.predators.controller;

import com.predators.dto.order.OrderMapper;
import com.predators.dto.order.OrderRequestDto;
import com.predators.dto.order.OrderResponseDto;
import com.predators.dto.orderitem.OrderItemMapper;
import com.predators.dto.orderitem.OrderItemResponseDto;
import com.predators.entity.Order;
import com.predators.entity.enums.OrderStatus;
import com.predators.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    @GetMapping
    @Override
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<OrderResponseDto> list = orderService.getAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/history")
    @Override
    public ResponseEntity<List<OrderItemResponseDto>> getOrderHistory() {
        List<OrderItemResponseDto> list = orderService.getHistory()
                .stream()
                .map(Order::getOrderItems)
                .flatMap(List::stream)
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }


    @GetMapping("/get-status/{status}")
    @Override
    public ResponseEntity<List<Order>> getStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getAllByStatus(OrderStatus.valueOf(status.toUpperCase())));
    }

    @PostMapping
    @Override
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderRequestDto dto) {
        Order created = orderService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.toDto(created));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long id) {
        Order order = orderService.getById(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<OrderResponseDto> updateStatus(@PathVariable Long id,
                                                         @RequestParam String status) {
        Order updated = orderService.updateStatus(id, OrderStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(orderMapper.toDto(updated));
    }

    @GetMapping("/{id}/status")
    @Override
    public ResponseEntity<String> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getStatus(id));
    }

    @PostMapping("/{id}/cancel")
    @Override
    public ResponseEntity<OrderResponseDto> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderMapper.toDto(orderService.cancel(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }
}

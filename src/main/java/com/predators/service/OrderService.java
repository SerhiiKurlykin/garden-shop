package com.predators.service;

import com.predators.dto.order.OrderRequestDto;
import com.predators.entity.Order;
import com.predators.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getById(Long id);

    Order create(OrderRequestDto dto);

    void delete(Long id);

    Order updateStatus(Long id, OrderStatus status);

    String getStatus(Long id);

    List<Order> getAllByStatus(OrderStatus status);

    List<Order> getHistory();
}

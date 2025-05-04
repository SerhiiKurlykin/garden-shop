package com.predators.service;

import com.predators.entity.OrderItem;
import java.util.List;

public interface OrderItemService {
     OrderItem create (OrderItem orderItem);

     List<OrderItem> saveAll(List<OrderItem> orderItems);
}

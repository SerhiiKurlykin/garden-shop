package com.predators.dto.converter;

import com.predators.dto.orderitem.OrderItemRequestDto;
import com.predators.dto.orderitem.OrderItemResponseDto;
import com.predators.entity.OrderItem;

public class OrderItemConverter implements Converter<OrderItemRequestDto, OrderItemResponseDto, OrderItem>{

    @Override
    public OrderItemResponseDto toDto(OrderItem orderItem) {
        return null;
    }

    @Override
    public OrderItem toEntity(OrderItemRequestDto orderItemRequestDto) {
        return null;
    }
}

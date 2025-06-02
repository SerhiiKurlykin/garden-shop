package com.predators.dto.orderitem;

import com.predators.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    public abstract OrderItemResponseDto toDto(OrderItem orderItem);
}

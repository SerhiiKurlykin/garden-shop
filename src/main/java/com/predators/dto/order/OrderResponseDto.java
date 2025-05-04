package com.predators.dto.order;

import com.predators.entity.OrderItem;
import com.predators.entity.enums.DeliveryMethod;
import lombok.Builder;
import java.sql.Timestamp;
import java.util.List;

@Builder
public record OrderResponseDto(
        Long id,
        Long userId,
        String status,
        String contactPhone,
        String deliveryAddress,
        DeliveryMethod deliveryMethod,
        List<OrderItem> items,
        Timestamp createdAt,
        Timestamp updatedAt
) {}

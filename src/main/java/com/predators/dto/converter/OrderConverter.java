package com.predators.dto.converter;

import com.predators.dto.order.OrderRequestDto;
import com.predators.dto.order.OrderResponseDto;
import com.predators.entity.Order;
import com.predators.entity.ShopUser;
import com.predators.entity.enums.DeliveryMethod;
import com.predators.entity.enums.OrderStatus;
import com.predators.service.ShopUserService;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class OrderConverter implements Converter<OrderRequestDto, OrderResponseDto, Order> {

    private final ShopUserService shopUserService;

    public OrderConverter(ShopUserService shopUserService) {
        this.shopUserService = shopUserService;
    }

    public Order toEntity(OrderRequestDto dto) {
        ShopUser user = shopUserService.getCurrentUser();
        return Order.builder()
                .user(user)
                .deliveryAddress(dto.deliveryAddress())
                .deliveryMethod(Enum.valueOf(DeliveryMethod.class, dto.deliveryMethod().toUpperCase()))
                .createdAt(Timestamp.from(Instant.now()))
                .contactPhone(user.getPhoneNumber())
                .status(OrderStatus.CREATED)
                .build();
    }

    public OrderResponseDto toDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .status(order.getStatus().name())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryMethod(order.getDeliveryMethod())
                .contactPhone(order.getContactPhone())
                .items(order.getOrderItems())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}

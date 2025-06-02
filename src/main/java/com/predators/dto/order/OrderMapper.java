package com.predators.dto.order;

import com.predators.dto.orderitem.OrderItemMapper;
import com.predators.dto.orderitem.OrderItemResponseDto;
import com.predators.entity.Order;
import com.predators.entity.enums.DeliveryMethod;
import com.predators.entity.enums.OrderStatus;
import com.predators.service.ShopUserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public abstract class OrderMapper {

    @Autowired
    protected OrderItemMapper orderItemMapper;

    @Autowired
    protected ShopUserService shopUserService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(shopUserService.getCurrentUser())")
    @Mapping(target = "deliveryMethod", source = "deliveryMethod", qualifiedByName = "stringToDeliveryMethod")
    @Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
    @Mapping(target = "contactPhone", expression = "java(shopUserService.getCurrentUser().getPhoneNumber())")
    @Mapping(target = "status", expression = "java(com.predators.entity.enums.OrderStatus.CREATED)")
    public abstract Order toEntity(OrderRequestDto dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "status", target = "status", qualifiedByName = "orderStatusToString")
    @Mapping(source = "orderItems", target = "items", qualifiedByName = "mapOrderItems")
    public abstract OrderResponseDto toDto(Order order);

    @Named("stringToDeliveryMethod")
    protected DeliveryMethod stringToDeliveryMethod(String deliveryMethod) {
        if (deliveryMethod == null) {
            return null;
        }
        return Enum.valueOf(DeliveryMethod.class, deliveryMethod.toUpperCase());
    }

    @Named("orderStatusToString")
    protected String orderStatusToString(OrderStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("mapOrderItems")
    protected List<OrderItemResponseDto> mapOrderItems(List<com.predators.entity.OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }
}

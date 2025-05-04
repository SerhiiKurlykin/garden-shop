package com.predators.util;

import com.predators.entity.Order;
import com.predators.entity.enums.OrderStatus;
import com.predators.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;
    private final Executor pool;

    @Async("pool")
    @Scheduled(fixedRate = 30000)
    public void changeStatusToPending() {
        List<Order> allByStatus = orderService.getAllByStatus(OrderStatus.CREATED);
        allByStatus.forEach(order -> orderService.updateStatus(order.getId(), OrderStatus.PENDING));
    }

    @Async("pool")
    @Scheduled(fixedRate = 30000)
    public void changeStatusToPaid() {
        Random random = new Random();
        List<Order> allByStatus = orderService.getAllByStatus(OrderStatus.PENDING);
        allByStatus.forEach(order -> {
            OrderStatus status = random.nextBoolean() ? OrderStatus.CANCELLED : OrderStatus.PAID;
            orderService.updateStatus(order.getId(), status);
        });
    }

    @Async("pool")
    @Scheduled(fixedRate = 30000)
    public void changeStatusToDelivery() {
        List<Order> allByStatus = orderService.getAllByStatus(OrderStatus.PAID);
        allByStatus.forEach(order -> orderService.updateStatus(order.getId(), OrderStatus.DELIVERY));
    }

    @Async("pool")
    @Scheduled(fixedRate = 30000)
    public void changeStatusToCompleted() {
        List<Order> allByStatus = orderService.getAllByStatus(OrderStatus.DELIVERY);
        allByStatus.forEach(order -> orderService.updateStatus(order.getId(), OrderStatus.COMPLETED));
    }
}

package com.predators.service;

import com.predators.entity.Order;
import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderService orderService;

    @Value("${orders.top.limit:10}")
    private int topLimit;

    @Override
    public List<Product> topItems(OrderStatus status) {
        List<Order> orders = orderService.getAllByStatus(status);


        Map<Product, Integer> productCounts = new HashMap<>();
        orders.forEach(order -> {
            order.getOrderItems().forEach(item -> {
                Product product = item.getProduct();
                productCounts.put(product, productCounts.getOrDefault(product, 0) + 1);
            });
        });

        List<Map.Entry<Product, Integer>> sortedList = new ArrayList<>(productCounts.entrySet());
        sortedList.sort(Map.Entry.<Product, Integer>comparingByValue().reversed());

        List<Product> topProducts = new ArrayList<>();
        int limit = Math.min(topLimit, sortedList.size());
        for (int i = 0; i < limit; i++) {
            topProducts.add(sortedList.get(i).getKey());
        }
        return topProducts;
    }

    @Override
    public Set<Product> waitingPaymentMoreNDays(Long days) {
        List<Order> orders = orderService.getAllByStatus(OrderStatus.PENDING);

        Set<Product> products = new HashSet<>();
        for (Order order : orders) {
            Timestamp updatedAt = order.getUpdatedAt();
            LocalDateTime dateTime = updatedAt.toLocalDateTime();
            LocalDateTime daysPlusN = dateTime.plusDays(days);
            LocalDateTime now = LocalDateTime.now();
            if (daysPlusN.isBefore(now)) {
                order.getOrderItems().forEach(item -> {
                    Product product = item.getProduct();
                    products.add(product);
                });
            }
        }
        return products;
    }
}


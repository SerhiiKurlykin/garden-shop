package com.predators.service;

import com.predators.entity.Order;
import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;
import com.predators.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderService orderService;

    @Value("${orders.top.limit:10}")
    private int topLimit;

    @Override
    public List<Product> topItems(OrderStatus status) {
        List<Order> orders = orderService.getAllByStatus(status);
        if (orders.isEmpty()) {
            throw  new OrderNotFoundException("List of orders is empty");
        }

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
}

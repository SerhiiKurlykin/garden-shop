package com.predators.service;

import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;

import java.util.List;
import java.util.Set;


public interface ReportService {

    List<Product> topItems(OrderStatus status);
    Set<Product> waitingPaymentMoreNDays(Long days);
}

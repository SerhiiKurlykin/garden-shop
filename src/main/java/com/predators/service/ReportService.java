package com.predators.service;

import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;

import java.util.List;


public interface ReportService {

    List<Product> topItems(OrderStatus status);
}

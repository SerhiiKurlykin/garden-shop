package com.predators.service;

import com.predators.dto.product.ProductCountDto;
import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public interface ReportService {

    List<ProductCountDto> topItems(OrderStatus status);

    Set<Product> waitingPaymentMoreNDays(Long days);

    BigDecimal getProfit(Long days, Long month, Long year);
}

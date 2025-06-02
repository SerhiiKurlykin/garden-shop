package com.predators.service;

import com.predators.dto.product.ProductCountDto;
import com.predators.entity.Order;
import com.predators.entity.OrderItem;
import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderService orderService;

    private final ProductService productService;

    @Value("${orders.top.limit:10}")
    private int limit;

    @Override
    public List<ProductCountDto> topItems(OrderStatus status) {
        return productService.findTopProductsAndCountsByOrderStatus(status, limit);
    }

    public Set<Product> waitingPaymentMoreNDays(Long days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = now.minusDays(days);
        Timestamp dateBefore = Timestamp.valueOf(dateTime);

        return productService.findByStatusAndUpdatedAtBeforeThreshold(OrderStatus.PENDING, dateBefore);
    }

    @Override
    public BigDecimal getProfit(Long days, Long month, Long year) {
        LocalDateTime date = LocalDateTime.now();
        if (days != null) {
            date = date.minusDays(days);
        }
        if (month != null) {
            date = date.minusMonths(month);
        }
        if (year != null) {
            date = date.minusYears(year);
        }
        List<Order> allByStatusAndDate = orderService.getAllByStatusAndAfterDate(OrderStatus.COMPLETED, Timestamp.valueOf(date));
        return allByStatusAndDate.stream()
                .map(Order::getOrderItems).flatMap(List::stream)
                .map(OrderItem::getPriceAtPurchase)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

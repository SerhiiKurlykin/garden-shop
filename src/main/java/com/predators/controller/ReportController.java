package com.predators.controller;

import com.predators.dto.product.ProductCountDto;
import com.predators.dto.product.ProductMapper;
import com.predators.dto.product.ProductResponseDto;
import com.predators.entity.enums.OrderStatus;
import com.predators.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/report")
@RequiredArgsConstructor
public class ReportController implements ReportApi {

    private final ReportService reportService;

    private final ProductMapper productMapper;

    @GetMapping("/top-product")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<List<ProductCountDto>> getTopProduct(@RequestParam String status) {
        List<ProductCountDto> list = reportService.topItems(OrderStatus.valueOf(status.toUpperCase()));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/waiting-payment")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Set<ProductResponseDto>> waitingPaymentMoreNDays(@RequestParam Long days) {
        Set<ProductResponseDto> products = reportService.waitingPaymentMoreNDays(days).stream()
                .map(productMapper::toDto).collect(Collectors.toSet());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/profit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<BigDecimal> getProfit(@RequestParam(name = "day", required = false) Long day,
                                                @RequestParam(name = "month", required = false) Long month,
                                                @RequestParam(name = "year", required = false) Long year) {
        BigDecimal profit = reportService.getProfit(day, month, year);
        return new ResponseEntity<>(profit, HttpStatus.OK);
    }
}

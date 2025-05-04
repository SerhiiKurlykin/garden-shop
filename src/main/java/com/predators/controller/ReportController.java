package com.predators.controller;

import com.predators.dto.converter.ProductConverter;
import com.predators.dto.product.ProductResponseDto;
import com.predators.entity.enums.OrderStatus;
import com.predators.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    private final ProductConverter converter;

    @GetMapping("/top-product")
    public ResponseEntity<List<ProductResponseDto>> getTopProduct(@RequestParam String status) {
        List<ProductResponseDto> list = reportService.topItems(OrderStatus.valueOf(status.toUpperCase())).stream().map(converter::toDto).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

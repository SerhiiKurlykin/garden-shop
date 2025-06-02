package com.predators.exception;

public class DiscountGraterThanPriceException extends RuntimeException {

    public DiscountGraterThanPriceException(String message) {
        super(message);
    }
}

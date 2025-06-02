package com.predators.exception;

public class ProductNotFoundException extends NotFoundInAppException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}

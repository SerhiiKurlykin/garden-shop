package com.predators.exception;

public class OrderNotFoundException extends NotFoundInAppException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}

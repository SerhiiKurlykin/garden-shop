package com.predators.exception;

public class CartItemNotFoundException extends NotFoundInAppException {

    public CartItemNotFoundException(String message) {
        super(message);
    }
}

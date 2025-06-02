package com.predators.exception;

public class ImpossibleChangeCurrentOrderStatusException extends RuntimeException {

    public ImpossibleChangeCurrentOrderStatusException(String message) {
        super(message);
    }
}

package com.predators.exception;

public abstract class NotFoundInAppException extends RuntimeException {

    public NotFoundInAppException(String message) {
        super(message);
    }
}

package com.predators.exception;

public class NotCurrentClientCartException extends RuntimeException{

    public NotCurrentClientCartException(String message) {
        super(message);
    }
}

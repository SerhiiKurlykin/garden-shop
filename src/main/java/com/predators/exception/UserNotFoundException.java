package com.predators.exception;

public class UserNotFoundException extends NotFoundInAppException {

    public UserNotFoundException(String message) {
        super(message);
    }
}

package com.predators.exception;

public class FavoriteNotFoundException extends NotFoundInAppException {

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}


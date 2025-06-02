package com.predators.exception;

public class CategoryNotFoundException extends NotFoundInAppException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}

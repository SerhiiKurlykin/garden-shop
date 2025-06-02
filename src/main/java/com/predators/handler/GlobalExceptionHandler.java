package com.predators.handler;

import com.predators.exception.AlreadyExistsException;
import com.predators.exception.CartIsEmptyException;
import com.predators.exception.DiscountGraterThanPriceException;
import com.predators.exception.ImpossibleChangeCurrentOrderStatusException;
import com.predators.exception.NotCurrentClientCartException;
import com.predators.exception.NotFoundInAppException;
import com.predators.exception.PermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundInAppException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AlreadyExistsException.class,
            DiscountGraterThanPriceException.class})
    public ResponseEntity<Object> handlerAlreadyExistsException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({CartIsEmptyException.class})
    public ResponseEntity<Object> handlerEntityIsEmptyException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotCurrentClientCartException.class})
    public ResponseEntity<Object> handlerNotCurrentClientCartException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({PermissionDeniedException.class})
    public ResponseEntity<Object> handlerPermissionDeniedException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ImpossibleChangeCurrentOrderStatusException.class})
    public ResponseEntity<Object> handlerImpossibleChangeCurrentOrderStatusException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}

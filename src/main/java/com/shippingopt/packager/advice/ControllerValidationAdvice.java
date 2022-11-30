package com.shippingopt.packager.advice;

import com.shippingopt.packager.exception.PriceValidationException;
import com.shippingopt.packager.exception.NoSuitableItemException;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class ControllerValidationAdvice {

    // generic validation exception management
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> badRequest(MethodArgumentNotValidException exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        log.error(message.getMessage(), exception);
        return ResponseEntity.badRequest().body(message);
    }

    // custom validation exception management
    @ExceptionHandler(PriceValidationException.class)
    public ResponseEntity<ErrorMessage> priceValidation(PriceValidationException exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        log.error(message.getMessage(), exception);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(NoSuitableItemException.class)
    public ResponseEntity<ErrorMessage> emptyFilteredListValidation(NoSuitableItemException exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        log.error(message.getMessage(), exception);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> genericException(Exception exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        log.error(message.getMessage(), exception);
        return ResponseEntity.internalServerError().body(message);
    }

}

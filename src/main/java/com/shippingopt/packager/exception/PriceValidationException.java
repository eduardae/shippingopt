package com.shippingopt.packager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceValidationException extends Exception {
    private final String message;
}

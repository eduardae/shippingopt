package com.shippingopt.packager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoSuitableItemException extends Exception {
    private final String message;
}

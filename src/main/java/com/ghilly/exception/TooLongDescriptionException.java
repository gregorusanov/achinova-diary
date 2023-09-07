package com.ghilly.exception;

public class TooLongDescriptionException extends RuntimeException {
    public TooLongDescriptionException(String message) {
        super(message);
    }
}

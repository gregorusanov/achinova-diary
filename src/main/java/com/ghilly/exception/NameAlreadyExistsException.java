package com.ghilly.exception;

public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String message){
        super(message);
    }
}

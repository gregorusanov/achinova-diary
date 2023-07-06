package com.ghilly.exception;

public class EmptyNameException extends RuntimeException{
    public EmptyNameException(String message){
        super(message);
    }
}

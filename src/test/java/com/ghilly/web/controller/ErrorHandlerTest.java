package com.ghilly.web.controller;

import com.ghilly.exception.IdIsNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ErrorHandlerTest {
    ErrorHandler handler = new ErrorHandler();
    int id = 100;
    String usa ="USA";
    HttpStatus status;

    @Test
    void catchRuntimeExceptionTest() {
        ResponseEntity<String> actual = handler.catchRuntimeException(new RuntimeException());
        status = HttpStatus.INTERNAL_SERVER_ERROR;

        assertEquals(status, actual.getStatusCode());
        assertEquals("Runtime exception", actual.getBody());
    }

    @Test
    void catchIdIsNotFoundExceptionTest() {
        String message = "The country with this ID " + id + " is not found.";
        ResponseEntity<String> actual = handler.catchIdIsNotFoundException
                (new IdIsNotFoundException(message));
        status = HttpStatus.NOT_FOUND;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchNameAlreadyExistsExceptionTest() {
        String message = "The country with this ID " + usa + " is not found.";
        ResponseEntity<String> actual = handler.catchNameAlreadyExistsException
                (new NameAlreadyExistsException(message));
        status = HttpStatus.CONFLICT;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }
}
package com.ghilly.web;

import com.ghilly.exception.IdIsNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.web.ErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ErrorHandlerTest {
    ErrorHandler handler = new ErrorHandler();
    int id = 100;
    String usa = "USA";

    @Test
    void catchRuntimeExceptionTest() {
        RuntimeException exception = new RuntimeException();
        ResponseEntity<String> actual = handler.catchRuntimeException(exception);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        assertEquals(status, actual.getStatusCode());
        assertEquals("Runtime exception " + exception.getMessage(), actual.getBody());
    }

    @Test
    void catchIdIsNotFoundExceptionTest() {
        String message = "The country with this ID " + id + " is not found.";
        ResponseEntity<String> actual = handler.catchIdIsNotFoundException
                (new IdIsNotFoundException(message));
        HttpStatus status = HttpStatus.NOT_FOUND;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchNameAlreadyExistsExceptionTest() {
        String message = "The country with this ID " + usa + " is not found.";
        ResponseEntity<String> actual = handler.catchNameAlreadyExistsException
                (new NameAlreadyExistsException(message));
        HttpStatus status = HttpStatus.CONFLICT;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }
}
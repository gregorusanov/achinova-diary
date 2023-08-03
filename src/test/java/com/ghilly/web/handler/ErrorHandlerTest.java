package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.web.handler.ErrorHandler;
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
                (new IdNotFoundException(message));
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

    @Test
    void catchWrongNameExceptionTest() {
        String wrongName = "U.S.A.";
        String message = "This field should contain only letters, that could be separated by one space or " +
                "one hyphen. " + wrongName + " is not allowed here!";
        ResponseEntity<String> actual = handler.catchWrongArgumentNameException(new WrongNameException(message));
        HttpStatus status = HttpStatus.BAD_REQUEST;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }
}
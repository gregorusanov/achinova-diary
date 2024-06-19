package com.ghilly.web.handler;

import com.ghilly.exception.*;
import com.ghilly.web.ErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ErrorHandlerTest {
    ErrorHandler handler = new ErrorHandler();
    int id = 100;
    String usa = "usa";

    @Test
    void catchRuntimeExceptionTest() {
        RuntimeException exception = new RuntimeException();
        ResponseEntity<String> actual = handler.catchRuntimeException(exception);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        assertEquals(status, actual.getStatusCode());
        assertEquals("Runtime exception: " + exception.getMessage(), actual.getBody());
    }

    @Test
    void catchIdIsNotFoundExceptionTest() {
        String message = "The country ID " + id + " is not found.";
        ResponseEntity<String> actual = handler.catchIdIsNotFoundException(new IdNotFoundException(message));
        HttpStatus status = HttpStatus.NOT_FOUND;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchNameAlreadyExistsExceptionTest() {
        String message = "The country name " + usa + " is not found.";
        ResponseEntity<String> actual =
                handler.catchThisAlreadyExistsException(new NameAlreadyExistsException(message));
        HttpStatus status = HttpStatus.BAD_REQUEST;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchWrongNameExceptionTest() {
        String wrongName = "U.S.A.";
        String message = "Warning! \n The legal name consists of letters that " +
                "could be separated by one space or hyphen. \n The name is not allowed here: " + wrongName;
        ResponseEntity<String> actual = handler.catchNotAcceptableException(new WrongNameException(message));
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchCapitalAlreadyExistsException() {
        String message = "The capital for the country ID 13 is already set. Try to update this city.";
        ResponseEntity<String> actual =
                handler.catchThisAlreadyExistsException(new CapitalAlreadyExistsException(message));
        HttpStatus status = HttpStatus.BAD_REQUEST;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchWrongDateException() {
        String message = "The arrival date should be earlier than departure date or should be equal to it!";
        ResponseEntity<String> actual =
                handler.catchNotAcceptableException(new IllegalDateException(message));
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchIllegalBudgetEx() {
        String message = "The budget should not be less than 0.";
        ResponseEntity<String> actual =
                handler.catchNotAcceptableException(new IllegalBudgetException(message));
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchIllegalRatingNumberEx() {
        String message = "The rating should be in the range from 0 to 10, including these numbers.";
        ResponseEntity<String> actual =
                handler.catchNotAcceptableException(new IllegalRatingNumberException(message));
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }

    @Test
    void catchTooLongDescriptionException() {
        String message = "The description should be no longer than 300 symbols, including spaces.";
        ResponseEntity<String> actual =
                handler.catchNotAcceptableException(new TooLongDescriptionException(message));
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

        assertEquals(status, actual.getStatusCode());
        assertEquals(message, actual.getBody());
    }
}
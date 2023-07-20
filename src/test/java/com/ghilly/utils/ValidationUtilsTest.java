package com.ghilly.utils;

import com.ghilly.exception.WrongArgumentNameException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


class ValidationUtilsTest {
    @ParameterizedTest
    @ValueSource(strings = {"USA", "Germany", "germanY", "Bosnia and Herzegovina", "Guinea-Bissau",
            "Guinea-Bissau-old-old"})
    void correctNamesCheckingTest(String name) {
        assertDoesNotThrow(() -> ValidationUtils.checkNameIsCorrect(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"U.S.A", "Germany*", "77Russia", "Bosnia  and Herzegovina", "Guinea-Bissau--old"})
    void incorrectNamesCheckingTest(String name) {
        WrongArgumentNameException exception = assertThrows(WrongArgumentNameException.class,
                () -> ValidationUtils.checkNameIsCorrect(name));
        assertEquals("This field should contain only letters, that could be separated by one space or " +
                "one hyphen. " + name + " is not allowed here!", exception.getMessage());
    }
}
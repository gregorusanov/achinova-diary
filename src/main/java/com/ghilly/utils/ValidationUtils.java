package com.ghilly.utils;

import com.ghilly.exception.WrongNameException;

import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static void checkNameIsCorrect(String name) {
        if (!Pattern.matches("^(?:[a-zA-Z]+[ -]?)+$", name))
            throw new WrongNameException
                    ("This field should contain only letters, that could be separated by one space or " +
                            "one hyphen. " + name + " is not allowed here!");
    }
}

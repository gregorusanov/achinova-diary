package com.ghilly.utils;

import com.ghilly.exception.WrongNameException;

import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isWrongName(String name) {
        return !Pattern.matches("^(?:[a-zA-Z]+[ -]?)+$", name);
    }
}

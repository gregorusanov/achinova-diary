package com.ghilly.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static void checkNameIsCorrect(String name) {
        //examples of incorrect name: Russia777, *()_, U.S.A., Bosnia  and Herzegovina, Guinea--Bissau, Guinea_Bissau,
        // Netherlands/Holland
        if (!Pattern.matches("^(?:[a-zA-Z]+[ -]?)+$", name))
            throw new IllegalArgumentException
                    ("This field should contain only letters, that could be separated by one space or one hyphen!");
    }
}

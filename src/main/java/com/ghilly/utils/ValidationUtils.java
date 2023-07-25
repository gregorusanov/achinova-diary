package com.ghilly.utils;

import com.ghilly.exception.IdNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isWrongName(String name) {
        return !Pattern.matches("^(?:[a-zA-Z]+[ -]?)+$", name);
    }

    public static void checkIdExists(int id, CrudRepository repository, String message) {
        if (repository.findById(id).isEmpty())
            throw new IdNotFoundException(message);
    }
}

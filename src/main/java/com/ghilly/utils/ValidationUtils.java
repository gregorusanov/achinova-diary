package com.ghilly.utils;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.WrongNameException;
import org.springframework.data.repository.CrudRepository;

import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isWrongName(String name) {
        return !Pattern.matches("^(?:[a-zA-Z]+[ -]?)+$", name);
    }


    public static void checkIdExists(int id, CrudRepository repository, String typeOfAreaAndId) {
        if (repository.findById(id).isEmpty())
            throw new IdNotFoundException(typeOfAreaAndId + id + " is not found.");
    }

    public static void checkNameIsWrong(String name) {
        if (isWrongName(name)) {
            throw new WrongNameException("Warning! \n The legal name consists of letters that " +
                    "could be separated by one space or hyphen. \n The name is not allowed here: " + name);
        }
    }
}

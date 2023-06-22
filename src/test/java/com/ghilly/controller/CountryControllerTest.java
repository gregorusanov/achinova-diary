package com.ghilly.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CountryControllerTest {

    private CountryController controller;
    private static final int id = 10;
    private static final String name = "USSR";

    @BeforeEach
    void init() {
        controller = new CountryController();
    }


    @Test
    void createCountry() {

        assertEquals(name, controller.create(name));
    }

    @Test
    void getCountries() {
        List<String> expected = controller.getCountries();

        assertEquals(expected, new ArrayList<>());
    }

    @Test
    void getCountry() {
        String expected = "get country" + id;

        assertEquals(expected, controller.getCountry(id));
    }

    @Test
    void updateCountry() {
        String newName = "Russia";

        String expected = "update" + id + "newName " + newName;

        assertEquals(expected, controller.update(id, newName));
    }

    @Test
    void deleteCountry() {
        String expected = "delete" + id;

        assertEquals(expected, controller.delete(id));
    }
}
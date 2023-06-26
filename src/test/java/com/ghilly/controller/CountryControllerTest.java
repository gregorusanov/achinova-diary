package com.ghilly.controller;

import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    private CountryServiceRest service;
    private CountryController controller;
    private static final int ID = 100;
    private static final String NAME = "USSR";

    @BeforeEach
    void init() {
        service = mock(CountryServiceRest.class);
        controller = new CountryController(service);
    }

    @Test
    void createCountry() {
        controller.create(NAME);

        assertAll(
                () -> verify(service).add(NAME),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountries() {
        List<String> expected = List.of("Japan", "Russia", "Germany");
        when(service.getAllCountries()).thenReturn(expected);

        List<String> actual = controller.getCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(service).getAllCountries(),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountry() {
        String expected = "Belgium";
        when(service.getCountry(ID)).thenReturn(expected);

        String actual = controller.getCountry(ID);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(service).getCountry(ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateCountry() {
        String newName = "Russia";
        when(service.getCountry(ID)).thenReturn(NAME);

        controller.update(ID, newName);

        assertAll(
                () -> verify(service).getCountry(ID),
                () -> verify(service).upgrade(ID, newName),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void deleteCountry() {
        controller.delete(ID);

        assertAll(
                () -> verify(service).remove(ID),
                () -> verifyNoMoreInteractions(service)
        );
    }
}
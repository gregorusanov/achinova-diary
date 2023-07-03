package com.ghilly.controller;

import com.ghilly.model.Country;
import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    private static final int ID = 100;
    private static final String NAME = "USSR";
    private static final Country USSR = new Country(ID, NAME);
    private CountryServiceRest service;
    private CountryController controller;

    @BeforeEach
    void init() {
        service = mock(CountryServiceRest.class);
        controller = new CountryController(service);
    }

    @Test
    void createCountry() {
        controller.create(NAME);

        assertAll(
                () -> verify(service).create(NAME),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountries() {
        Country usa = new Country(2, "USA");
        List<Country> expected = List.of(USSR, usa);
        when(service.getAllCountries()).thenReturn(expected);

        controller.getAllCountries();

        assertAll(
                () -> verify(service).getAllCountries(),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountry() {
        when(service.getCountryById(ID)).thenReturn(USSR);

        Country actual = controller.getCountry(ID).getBody();

        assertAll(
                () -> assertEquals(USSR, actual),
                () -> verify(service).getCountryById(ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateCountry() {
        String newName = "Russia";

        controller.update(ID, newName);

        assertAll(
                () -> verify(service).update(new Country(ID, newName)),
                () -> verify(service).getCountryById(ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void deleteCountry() {
        controller.delete(ID);

        assertAll(
                () -> verify(service).delete(ID),
                () -> verifyNoMoreInteractions(service)
        );
    }
}
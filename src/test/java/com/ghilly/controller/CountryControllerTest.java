package com.ghilly.controller;

import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    private static final CountryServiceRest service = mock(CountryServiceRest.class);
    private static final CountryController controller = new CountryController(service);
    private static final int id = 100;
    private static final String name = "USSR";


    @Test
    void createCountry() {
        controller.create(name);

        assertAll(
                () -> verify(service).add(name),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountries() {
        List<String> expected = new ArrayList<>(List.of("Japan", "Russia", "Germany"));
        when(service.getAll()).thenReturn(expected);

        List<String> actual = controller.getCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(service).getAll(),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountry() {
        String expected = "Belgium";
        when(service.getCountry(id)).thenReturn(expected);

        String actual = controller.getCountry(id);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(service, times(2)).getCountry(id),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateCountry() {
        String newName = "Russia";
        when(service.getCountry(id)).thenReturn(name);

        controller.update(id, newName);

        assertAll(
                () -> verify(service).getCountry(id),
                () -> verify(service).upgrade(id, newName),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void deleteCountry() {
        controller.delete(id);

        assertAll(
                () -> verify(service).remove(id),
                () -> verifyNoMoreInteractions(service)
        );
    }
}
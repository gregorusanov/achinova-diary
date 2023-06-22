package com.ghilly.controller;

import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@SpringBootTest
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
        controller.getCountries();

        assertAll(
                () -> verify(service).receiveList(),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountry() {
        controller.getCountry(id);

        assertAll(
                () -> verify(service).receiveCountry(anyInt()),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateCountry() {
        String newName = "Russia";

        controller.update(id, newName);

        assertAll(
                () -> verify(service).upgrade(id, newName),
                () ->verifyNoMoreInteractions(service)
        );

    }

    @Test
    void deleteCountry() {
        controller.delete(id);

        assertAll(
                () -> verify(service).clear(id),
                () -> verifyNoMoreInteractions(service)
        );

    }
}
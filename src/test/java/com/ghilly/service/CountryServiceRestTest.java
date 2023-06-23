package com.ghilly.service;

import com.ghilly.repository.CountryRepositoryRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CountryServiceRestTest {
    private static final String NAME = "USSR";
    private static final int ID = 1;
    private CountryRepositoryRest repository;
    private CountryServiceRest service;


    @BeforeEach
    void init() {
        repository = mock(CountryRepositoryRest.class);
        service = new CountryServiceRest(repository);
    }
    @Test
    void addAndReceiveCountry() {
        when(repository.takeCountry(ID)).thenReturn(NAME);

        service.add(NAME);

        assertAll(
                () -> assertEquals(NAME, repository.takeCountry(ID)),
                () -> verify(repository).insert(NAME)
        );
    }

    @Test
    void getAllCountries() {
        List<String> expected = new ArrayList<>(List.of("Afghanistan", "France", "China"));
        when(repository.takeAllCountries()).thenReturn(expected);

        List<String> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(repository).takeAllCountries(),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getCountrySuccess() {
        when(repository.containsCountry(ID)).thenReturn(true);
        when(repository.takeCountry(ID)).thenReturn(NAME);

        String expected = service.getCountry(ID);

        assertAll(
                () -> assertEquals(expected, NAME),
                () -> verify(repository).containsCountry(ID),
                () -> verify(repository).takeCountry(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getCountryFail() {
        when(repository.containsCountry(ID)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.getCountry(ID));

        assertAll(
                () -> assertEquals("The country is not found.", exception.getMessage()),
                () -> verify(repository).containsCountry(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void upgradeSuccess() {
        String newName = "Russia";
        when(repository.containsCountry(ID)).thenReturn(true);
        when(repository.takeCountry(ID)).thenReturn(NAME);

        service.upgrade(ID, newName);

        assertAll(
                () -> verify(repository).containsCountry(ID),
                () -> verify(repository).takeCountry(ID),
                () -> verify(repository).change(ID, newName),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void upgradeFail() {
        String newName = "Russia";
        when(repository.containsCountry(ID)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.upgrade(ID, newName));

        assertAll(
                () -> assertEquals("The country is not found.", exception.getMessage()),
                () -> verify(repository).containsCountry(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void removeSuccess() {
        when(repository.containsCountry(ID)).thenReturn(true);

        service.remove(ID);

        assertAll(
                () -> verify(repository).containsCountry(ID),
                () -> verify(repository).cut(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void removeFail() {
        when(repository.containsCountry(ID)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.remove(ID));

        assertAll(
                () -> assertEquals("The country is not found.", exception.getMessage()),
                () -> verify(repository).containsCountry(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }
}
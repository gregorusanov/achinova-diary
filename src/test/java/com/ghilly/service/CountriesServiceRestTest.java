package com.ghilly.service;

import com.ghilly.model.Countries;
import com.ghilly.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountriesServiceRestTest {
    private static final String NAME = "USSR";
    private static final int ID = 1;
    private static final Countries USSR = new Countries(ID, NAME);
    private CountryRepository repository;
    private CountryServiceRest service;

    @BeforeEach
    void init() {
        repository = mock(CountryRepository.class);
        service = new CountryServiceRest(repository);
    }

    @Test
    void addCountryFail() {
        when(repository.findByName(NAME)).thenReturn(Optional.of(USSR));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.create(NAME));

        assertAll(
                () -> assertEquals("Country with this name " + NAME + " already exists.", exception.getMessage()),
                () -> verify(repository).findByName(NAME),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void addCountrySuccess() {
        service.create(NAME);

        assertAll(
                () -> verify(repository).findByName(NAME),
                () -> verify(repository).save(new Countries(0, NAME)),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getAllCountries() {
        Countries af = new Countries("Afghanistan");
        Countries fr = new Countries("France");
        Countries cn = new Countries("China");
        List<Countries> expected = List.of(af, fr, cn);
        when(repository.findAll()).thenReturn(expected);

        List<Countries> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(repository).findAll(),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getCountrySuccess() {
        when(repository.findById(ID)).thenReturn(Optional.of(USSR));

        Countries expected = service.getCountryById(ID);

        assertAll(
                () -> assertEquals(expected, USSR),
                () -> verify((repository), times(2)).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getCountryFail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getCountryById(ID));

        assertAll(
                () -> assertEquals("Countries with this ID " + ID + " is not found.", exception.getMessage()),
                () -> verify(repository).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void upgradeSuccess() {
        String newName = "Russia";
        Countries countries = new Countries(ID, newName);
        when(repository.existsById(ID)).thenReturn(true);

        service.update(countries);

        assertAll(
                () -> verify(repository).existsById(ID),
                () -> verify(repository).save(countries),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void upgradeFail() {
        String newName = "Russia";
        Countries countries = new Countries(ID, newName);
        when(repository.existsById(ID)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.update(countries));

        assertAll(
                () -> assertEquals("The countries with the ID " + ID + " is not found.", exception.getMessage()),
                () -> verify(repository).existsById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void removeSuccess() {
        when(repository.existsById(ID)).thenReturn(true);

        service.delete(ID);

        assertAll(
                () -> verify(repository).existsById(ID),
                () -> verify(repository).deleteById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void removeFail() {
        when(repository.existsById(ID)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.delete(ID));

        assertAll(
                () -> assertEquals("The country with the ID " + ID + " is not found.", exception.getMessage()),
                () -> verify(repository).existsById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }
}
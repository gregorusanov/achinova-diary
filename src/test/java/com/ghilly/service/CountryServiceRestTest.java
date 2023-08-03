package com.ghilly.service;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.Country;
import com.ghilly.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceRestTest {
    private static final String NAME = "USSR";
    private static final int ID = 1;
    private static final Country USSR = new Country(ID, NAME);
    private final String idNotFoundExMsg = "The country ID " + ID + " is not found.";
    private final String wrongNameExMsg = "Warning! \n The legal name consists of letters that could be separated " +
            "by one space or hyphen. \n The name is not allowed here: ";
    private CountryRepository repository;
    private CountryServiceRest service;

    @BeforeEach
    void init() {
        repository = mock(CountryRepository.class);
        service = new CountryServiceRest(repository);
    }

    @Test
    void createCountryFail() {
        when(repository.findByName(NAME)).thenReturn(Optional.of(USSR));

        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> service.create(USSR));

        assertAll(
                () -> assertEquals("The country with this name " + NAME + " already exists.", exception.getMessage()),
                () -> verify(repository).findByName(NAME),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void createCountrySuccess() {
        service.create(USSR);

        assertAll(
                () -> verify(repository).findByName(NAME),
                () -> verify(repository).save(USSR),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void createCountryWithWrongNameFail() {
        Country usa = new Country("U.S.A.");

        WrongNameException exception = assertThrows(WrongNameException.class, () -> service.create(usa));

        assertAll(
                () -> assertEquals(wrongNameExMsg + usa.getName(), exception.getMessage()),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getAllCountries() {
        Country af = new Country("Afghanistan");
        Country fr = new Country("France");
        Country cn = new Country("China");
        List<Country> expected = List.of(af, fr, cn);
        when(repository.findAll()).thenReturn(expected);

        List<Country> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(repository).findAll(),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getCountrySuccess() {
        when(repository.findById(ID)).thenReturn(Optional.of(USSR));

        Country expected = service.getCountryById(ID);

        assertAll(
                () -> assertEquals(expected, USSR),
                () -> verify(repository, times(2)).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getCountryFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> service.getCountryById(ID));

        assertAll(
                () -> assertEquals(idNotFoundExMsg, exception.getMessage()),
                () -> verify((repository)).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void updateSuccess() {
        String newName = "Russia";
        Country country = new Country(ID, newName);
        when(repository.findById(ID)).thenReturn(Optional.of(USSR));

        service.update(country);

        assertAll(
                () -> verify(repository).findById(ID),
                () -> verify(repository).save(country),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void updateFailIdNotFound() {
        String newName = "Russia";
        Country country = new Country(ID, newName);

        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> service.update(country));

        assertAll(
                () -> assertEquals(idNotFoundExMsg, exception.getMessage()),
                () -> verify(repository).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void updateWrongNewNameFail() {
        String newName = "Russia!";
        Country country = new Country(ID, newName);
        when(repository.findById(ID)).thenReturn(Optional.of(USSR));

        WrongNameException exception = assertThrows(WrongNameException.class, () -> service.update(country));

        assertAll(
                () -> assertEquals(wrongNameExMsg + newName, exception.getMessage()),
                () -> verify(repository).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void removeSuccess() {
        when(repository.findById(ID)).thenReturn(Optional.of(USSR));

        service.delete(ID);

        assertAll(
                () -> verify(repository).findById(ID),
                () -> verify(repository).deleteById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void removeFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> service.delete(ID));

        assertAll(
                () -> assertEquals(idNotFoundExMsg, exception.getMessage()),
                () -> verify(repository).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }
}
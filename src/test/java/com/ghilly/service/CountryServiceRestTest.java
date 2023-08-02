package com.ghilly.service;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.Country;
import com.ghilly.repository.CityRepository;
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
    private final String idNotFoundExMsg = "The country with the ID " + ID + " is not found.";
    private final String wrongNameExMsg = "Warning! \n The legal country name consists of letters that could be separated " +
            "by one space or hyphen. \n The name is not allowed here: ";
    private CountryRepository countryRepository;
    private CountryServiceRest service;
    private CityRepository cityRepository;

    @BeforeEach
    void init() {
        countryRepository = mock(CountryRepository.class);
        cityRepository = mock(CityRepository.class);
        service = new CountryServiceRest(countryRepository, cityRepository);
    }

    @Test
    void createCountryFail() {
        when(countryRepository.findByName(NAME)).thenReturn(Optional.of(USSR));

        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> service.create(USSR));

        assertAll(
                () -> assertEquals("The country with this name " + NAME + " already exists.", exception.getMessage()),
                () -> verify(countryRepository).findByName(NAME),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void createCountrySuccess() {
        service.create(USSR);

        assertAll(
                () -> verify(countryRepository).findByName(NAME),
                () -> verify(countryRepository).save(USSR),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void createCountryWithWrongNameFail() {
        Country usa = new Country("U.S.A.");

        WrongNameException exception = assertThrows(WrongNameException.class, () -> service.create(usa));

        assertAll(
                () -> assertEquals(wrongNameExMsg + usa.getName(), exception.getMessage()),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getAllCountries() {
        Country af = new Country("Afghanistan");
        Country fr = new Country("France");
        Country cn = new Country("China");
        List<Country> expected = List.of(af, fr, cn);
        when(countryRepository.findAll()).thenReturn(expected);

        List<Country> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(countryRepository).findAll(),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCountrySuccess() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(USSR));

        Country expected = service.getCountryById(ID);

        assertAll(
                () -> assertEquals(expected, USSR),
                () -> verify(countryRepository, times(2)).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCountryFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> service.getCountryById(ID));

        assertAll(
                () -> assertEquals(idNotFoundExMsg, exception.getMessage()),
                () -> verify((countryRepository)).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void updateSuccess() {
        String newName = "Russia";
        Country country = new Country(ID, newName);
        when(countryRepository.findById(ID)).thenReturn(Optional.of(USSR));

        service.update(country);

        assertAll(
                () -> verify(countryRepository).findById(ID),
                () -> verify(countryRepository).save(country),
                () -> verifyNoMoreInteractions(countryRepository)
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
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void updateWrongNewNameFail() {
        String newName = "Russia!";
        Country country = new Country(ID, newName);
        when(countryRepository.findById(ID)).thenReturn(Optional.of(USSR));

        WrongNameException exception = assertThrows(WrongNameException.class, () -> service.update(country));

        assertAll(
                () -> assertEquals(wrongNameExMsg + newName, exception.getMessage()),
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void removeSuccess() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(USSR));

        service.delete(ID);

        assertAll(
                () -> verify(countryRepository).findById(ID),
                () -> verify(countryRepository).deleteById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void removeFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> service.delete(ID));

        assertAll(
                () -> assertEquals(idNotFoundExMsg, exception.getMessage()),
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }
}
package com.ghilly.service;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.City;
import com.ghilly.model.Country;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceRestTest {
    private static final String NAME = "Moscow";
    private static final int ID = 1;
    private static final Country RUS = new Country(ID, "Russia");
    private static final City MOS = new City(NAME, ID);
    private CityServiceRest service;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;

    @BeforeEach
    void init() {
        countryRepository = mock(CountryRepository.class);
        cityRepository = mock(CityRepository.class);
        service = new CityServiceRest(cityRepository, countryRepository);
    }

    @Test
    void createSuccess() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(RUS));

        service.create(NAME, ID);

        assertAll(
                () -> verify(countryRepository, times(2)).findById(ID),
                () -> verify(cityRepository).findByName(NAME),
                () -> verify(cityRepository).save(MOS),
                () -> verifyNoMoreInteractions(cityRepository),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void createFailIdIsNotFound() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> service.create(NAME, ID));

        assertAll(
                () -> assertEquals("The country with the ID " + ID + " is not found.", exception.getMessage()),
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void createFailNameAlreadyExists() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(RUS));
        when(cityRepository.findByName(NAME)).thenReturn(Optional.of(MOS));
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> service.create(NAME, ID));

        assertAll(
                () -> assertEquals("The city with the name " + NAME + " already exists!",
                        exception.getMessage()),
                () -> verify(countryRepository).findById(ID),
                () -> verify(cityRepository).findByName(NAME),
                () -> verifyNoMoreInteractions(countryRepository),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

}
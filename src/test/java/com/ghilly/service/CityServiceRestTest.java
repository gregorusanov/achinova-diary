package com.ghilly.service;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
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
    private static final String MOSCOW = "Moscow";
    private static final int ID = 1;
    private static final Country RUS = new Country(ID, "Russia");
    private static final City MOS = new City(MOSCOW, ID);
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

        service.create(MOSCOW, ID);

        assertAll(
                () -> verify(countryRepository, times(2)).findById(ID),
                () -> verify(cityRepository).findByName(MOSCOW),
                () -> verify(cityRepository).save(MOS),
                () -> verifyNoMoreInteractions(cityRepository, countryRepository)
        );
    }

    @Test
    void createIdIsNotFoundFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> service.create(MOSCOW, ID));

        assertAll(
                () -> assertEquals("The country with the ID " + ID + " is not found.", exception.getMessage()),
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void createNameAlreadyExistsFail() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(RUS));
        when(cityRepository.findByName(MOSCOW)).thenReturn(Optional.of(MOS));
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> service.create(MOSCOW, ID));

        assertAll(
                () -> assertEquals("The city with the name " + MOSCOW + " already exists.",
                        exception.getMessage()),
                () -> verify(countryRepository).findById(ID),
                () -> verify(cityRepository).findByName(MOSCOW),
                () -> verifyNoMoreInteractions(countryRepository, cityRepository)
        );
    }

    @Test
    void createWrongNameFail() {
        String wrong = "777Mo$cow!";
        when(countryRepository.findById(ID)).thenReturn(Optional.of(RUS));

        WrongNameException exception = assertThrows(WrongNameException.class, () -> service.create(wrong, ID));

        assertAll(
                () -> assertEquals("Warning! \n The legal country name consists of letters that could be " +
                                "separated by one space or hyphen. \n The name is not allowed here: " + wrong,
                        exception.getMessage()),
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }


}
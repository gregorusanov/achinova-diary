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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceRestTest {
    private static final String MOSCOW = "Moscow";
    private static final int COUNTRY_ID = 1;
    private static final int CITY_ID = 7;
    private static final Country RUS = new Country(COUNTRY_ID, "Russia");
    private static final City MOS = new City(MOSCOW, COUNTRY_ID);
    private static final String COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN = "The country with the ID ";
    private static final String CITY_ID_NOT_FOUND_EX_MSG_BEGIN = "The city with the ID ";
    private static final String ID_NOT_FOUND_EX_MSG_END = " is not found.";
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
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(RUS));

        service.create(MOSCOW, COUNTRY_ID);

        assertAll(
                () -> verify(countryRepository, times(2)).findById(COUNTRY_ID),
                () -> verify(cityRepository).findByName(MOSCOW),
                () -> verify(cityRepository).save(MOS),
                () -> verifyNoMoreInteractions(cityRepository, countryRepository)
        );
    }

    @Test
    void createIdIsNotFoundFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> service.create(MOSCOW, COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(countryRepository).findById(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void createNameAlreadyExistsFail() {
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(RUS));
        when(cityRepository.findByName(MOSCOW)).thenReturn(Optional.of(MOS));
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> service.create(MOSCOW, COUNTRY_ID));

        assertAll(
                () -> assertEquals("The city with the name " + MOSCOW + " already exists.",
                        exception.getMessage()),
                () -> verify(countryRepository).findById(COUNTRY_ID),
                () -> verify(cityRepository).findByName(MOSCOW),
                () -> verifyNoMoreInteractions(countryRepository, cityRepository)
        );
    }

    @Test
    void createWrongNameFail() {
        String wrong = "777Mo$cow!";
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(RUS));

        WrongNameException exception = assertThrows(WrongNameException.class, () -> service.create(wrong, COUNTRY_ID));

        assertAll(
                () -> assertEquals("Warning! \n The legal country name consists of letters that could be " +
                                "separated by one space or hyphen. \n The name is not allowed here: " + wrong,
                        exception.getMessage()),
                () -> verify(countryRepository).findById(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCitySuccess() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS));

        City city = service.getCity(CITY_ID);

        assertAll(
                () -> assertEquals(city.getName(), MOSCOW),
                () -> verify(cityRepository, times(2)).findById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void getCityIdNotFound() {
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(RUS));

        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> service.getCity(CITY_ID));

        assertAll(
                () -> assertEquals(CITY_ID_NOT_FOUND_EX_MSG_BEGIN + CITY_ID + ID_NOT_FOUND_EX_MSG_END,
                        ex.getMessage()),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void getAllCities() {
        String sochi = "Sochi";
        String spb = "Saint-Petersburg";
        List<City> cities = List.of(MOS, new City(sochi, COUNTRY_ID), new City(spb, COUNTRY_ID));
        when(cityRepository.findAll()).thenReturn(cities);

        List<City> actual = service.getAllCities();

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(actual.get(0).getName(), MOSCOW),
                () -> assertEquals(actual.get(1).getName(), sochi),
                () -> assertEquals(actual.get(2).getName(), spb),
                () -> verify(cityRepository).findAll(),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

}
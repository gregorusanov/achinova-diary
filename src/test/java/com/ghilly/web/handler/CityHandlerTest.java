package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.City;
import com.ghilly.model.Country;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import com.ghilly.service.CityServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityHandlerTest {
    private static final String MOSCOW = "Moscow";
    private static final int COUNTRY_ID = 1;
    private static final int CITY_ID = 7;
    private static final Country RUS = new Country(COUNTRY_ID, "Russia");
    private static final CityDAO MOS_DAO = new CityDAO(MOSCOW, RUS, true);
    private static final City MOS = new City(MOSCOW, true);
    private static final String COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN = "The country ID ";
    private static final String CITY_ID_NOT_FOUND_EX_MSG_BEGIN = "The city ID ";
    private static final String ID_NOT_FOUND_EX_MSG_END = " is not found.";
    private static final String WRONG_NAME_EX_MSG = "Warning! \n The legal name consists of letters that " +
            "could be separated by one space or hyphen. \n The name is not allowed here: ";
    private CityHandler handler;
    private CityServiceRest service;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;

    @BeforeEach
    void init() {
        countryRepository = mock(CountryRepository.class);
        cityRepository = mock(CityRepository.class);
        service = mock(CityServiceRest.class);
        handler = new CityHandler(service, cityRepository, countryRepository);
    }

    @Test
    void createSuccess() {
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(RUS));

        handler.create(MOS, COUNTRY_ID);

        assertAll(
                () -> verify(countryRepository, times(2)).findById(COUNTRY_ID),
                () -> verify(cityRepository).findByName(MOSCOW),
                () -> verify(service).create(MOS_DAO),
                () -> verifyNoMoreInteractions(cityRepository, countryRepository, service)
        );
    }

    @Test
    void createIdIsNotFoundFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> handler.create(MOS, COUNTRY_ID));

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
        when(cityRepository.findByName(MOSCOW)).thenReturn(Optional.of(MOS_DAO));
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.create(MOS, COUNTRY_ID));

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
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(RUS));
        String wrong = "777Mo$cow!";
        City city = new City(wrong);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.create(city, COUNTRY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + wrong,
                        exception.getMessage()),
                () -> verify(countryRepository).findById(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCitySuccess() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS_DAO));
        when(service.getCity(CITY_ID)).thenReturn(MOS_DAO);

        CityDAO cityDAO = handler.getCity(CITY_ID);

        assertAll(
                () -> assertEquals(cityDAO.getName(), MOSCOW),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verify(service).getCity(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository, service)
        );
    }

    @Test
    void getCityIdNotFound() {
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> handler.getCity(CITY_ID));

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
        List<CityDAO> cities = List.of(MOS_DAO, new CityDAO(sochi, RUS, false), new CityDAO(spb, RUS, false));
        when(service.getAllCities()).thenReturn(cities);

        List<CityDAO> actual = service.getAllCities();

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(actual.get(0).getName(), MOSCOW),
                () -> assertEquals(actual.get(1).getName(), sochi),
                () -> assertEquals(actual.get(2).getName(), spb),
                () -> verify(service).getAllCities(),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateSuccess() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS_DAO));
        String newName = "NeRezinovaya";
        City toChange = new City(CITY_ID, newName, true);
        CityDAO cityDAO = new CityDAO(CITY_ID, newName, RUS, true);

        handler.update(toChange, CITY_ID);

        assertAll(
                () -> verify(cityRepository, times(2)).findById(CITY_ID),
                () -> verify(cityRepository).findByName(newName),
                () -> verify(service).update(cityDAO),
                () -> verifyNoMoreInteractions(cityRepository, service)
        );
    }

    @Test
    void updateFailIdNotFound() {
        City toChange = new City(CITY_ID, "Moskvabad", true);

        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> handler.update(toChange, CITY_ID));

        assertAll(
                () -> assertEquals(CITY_ID_NOT_FOUND_EX_MSG_BEGIN + CITY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void updateWrongNewNameFail() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS_DAO));
        String newName = "Moskv@b@d";
        City toChange = new City(CITY_ID, newName, true);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.update(toChange, CITY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + newName, exception.getMessage()),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verifyNoMoreInteractions(countryRepository, cityRepository)
        );
    }

    @Test
    void updateExistingNewNameFail() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS_DAO));
        String newName = "Saint-Petersburg";
        CityDAO spb = new CityDAO(10, newName, RUS, false);
        when(cityRepository.findByName(newName)).thenReturn(Optional.of(spb));
        City toChange = new City(CITY_ID, newName, true);

        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.update(toChange, CITY_ID));

        assertAll(
                () -> assertEquals("The city with the name " + newName + " already exists.",
                        exception.getMessage()),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verify(cityRepository).findByName(newName),
                () -> verifyNoMoreInteractions(countryRepository, cityRepository)
        );
    }

    @Test
    void deleteSuccess() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS_DAO));

        handler.delete(CITY_ID);

        assertAll(
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verify(service).delete(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository, service)
        );
    }

    @Test
    void deleteFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> handler.delete(CITY_ID));

        assertAll(
                () -> assertEquals(CITY_ID_NOT_FOUND_EX_MSG_BEGIN + CITY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }
}
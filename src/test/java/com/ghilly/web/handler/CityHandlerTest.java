package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.City;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityHandlerTest {
    private static final String MOSCOW = "Moscow";
    private static final int COUNTRY_ID = 1;
    private static final int CITY_ID = 7;
    private static final CountryDAO RUS = new CountryDAO(COUNTRY_ID, "Russia");
    private static final CityDAO MOS_DAO = new CityDAO(MOSCOW, RUS, true);
    private static final City MOS = new City(MOSCOW, true);
    private static final String COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN = "The country ID ";
    private static final String CITY_ID_NOT_FOUND_EX_MSG_BEGIN = "The city ID ";
    private static final String ID_NOT_FOUND_EX_MSG_END = " is not found.";
    private static final String WRONG_NAME_EX_MSG = "Warning! \n The legal name consists of letters that " +
            "could be separated by one space or hyphen. \n The name is not allowed here: ";
    private CityHandler handler;
    private CityServiceRest cityServiceRest;
    private CountryServiceRest countryServiceRest;

    @BeforeEach
    void init() {
        countryServiceRest = mock(CountryServiceRest.class);
        cityServiceRest = mock(CityServiceRest.class);
        handler = new CityHandler(cityServiceRest, countryServiceRest);
    }

    @Test
    void createSuccess() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS);

        handler.create(MOS, COUNTRY_ID);

        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(cityServiceRest).cityNameExists(MOSCOW),
                () -> verify(countryServiceRest).getCountryById(COUNTRY_ID),
                () -> verify(cityServiceRest).create(MOS_DAO),
                () -> verifyNoMoreInteractions(cityServiceRest, countryServiceRest)
        );
    }

    @Test
    void createIdIsNotFoundFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> handler.create(MOS, COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void createNameAlreadyExistsFail() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS);
        when(cityServiceRest.cityNameExists(MOSCOW)).thenReturn(true);
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.create(MOS, COUNTRY_ID));

        assertAll(
                () -> assertEquals("The city name " + MOSCOW + " already exists.",
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(cityServiceRest).cityNameExists(MOSCOW),
                () -> verifyNoMoreInteractions(countryServiceRest, cityServiceRest)
        );
    }

    @Test
    void createWrongNameFail() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        String wrong = "777Mo$cow!";
        City city = new City(wrong);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.create(city, COUNTRY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + wrong,
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getCitySuccess() {
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);
        when(cityServiceRest.getCity(CITY_ID)).thenReturn(MOS_DAO);

        CityDAO cityDAO = handler.getCity(CITY_ID);

        assertAll(
                () -> assertEquals(cityDAO.getName(), MOSCOW),
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verify(cityServiceRest).getCity(CITY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void getCityIdNotFound() {
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> handler.getCity(CITY_ID));

        assertAll(
                () -> assertEquals(CITY_ID_NOT_FOUND_EX_MSG_BEGIN + CITY_ID + ID_NOT_FOUND_EX_MSG_END,
                        ex.getMessage()),
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void getAllCities() {
        String sochi = "Sochi";
        String spb = "Saint-Petersburg";
        List<CityDAO> cities = List.of(MOS_DAO, new CityDAO(sochi, RUS, false), new CityDAO(spb, RUS, false));
        when(cityServiceRest.getAllCities()).thenReturn(cities);

        List<CityDAO> actual = cityServiceRest.getAllCities();

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(actual.get(0).getName(), MOSCOW),
                () -> assertEquals(actual.get(1).getName(), sochi),
                () -> assertEquals(actual.get(2).getName(), spb),
                () -> verify(cityServiceRest).getAllCities(),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void updateSuccess() {
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);
        when(cityServiceRest.getCity(CITY_ID)).thenReturn(MOS_DAO);
        String newName = "NeRezinovaya";
        City toChange = new City(CITY_ID, newName, true);
        CityDAO cityDAO = new CityDAO(CITY_ID, newName, RUS, true);

        handler.update(toChange, CITY_ID);

        assertAll(
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verify(cityServiceRest).cityNameExists(newName),
                () -> verify(cityServiceRest).getCity(CITY_ID),
                () -> verify(cityServiceRest).update(cityDAO),
                () -> verifyNoMoreInteractions(cityServiceRest)
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
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void updateWrongNewNameFail() {
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);
        String newName = "Moskv@b@d";
        City toChange = new City(CITY_ID, newName, true);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.update(toChange, CITY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + newName, exception.getMessage()),
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void updateExistingNewNameFail() {
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);
        String newName = "Saint-Petersburg";
        when(cityServiceRest.cityNameExists(newName)).thenReturn(true);
        City toChange = new City(CITY_ID, newName, false);

        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.update(toChange, CITY_ID));

        assertAll(
                () -> assertEquals("The city name " + newName + " already exists.",
                        exception.getMessage()),
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verify(cityServiceRest).cityNameExists(newName),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void deleteSuccess() {
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);

        handler.delete(CITY_ID);

        assertAll(
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verify(cityServiceRest).delete(CITY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void deleteFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> handler.delete(CITY_ID));

        assertAll(
                () -> assertEquals(CITY_ID_NOT_FOUND_EX_MSG_BEGIN + CITY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }
}
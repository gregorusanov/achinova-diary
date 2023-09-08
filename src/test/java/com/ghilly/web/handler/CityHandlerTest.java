package com.ghilly.web.handler;

import com.ghilly.exception.CapitalAlreadyExistsException;
import com.ghilly.exception.CityAlreadyExistsException;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityHandlerTest {
    private static final String MOSCOW = "moscow";
    private static final int COUNTRY_ID = 1;
    private static final int CITY_ID = 7;
    private static final CountryDAO RUS = new CountryDAO(COUNTRY_ID, "russia");
    private static final CityDAO MOS_DAO_FROM_REPO = new CityDAO(CITY_ID, MOSCOW, RUS, true);
    private static final CityDAO MOS_DAO = new CityDAO(MOSCOW, true);
    private static final String COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN = "The country ID ";
    private static final String CITY_ID_NOT_FOUND_EX_MSG_BEGIN = "The city ID ";
    private static final String ID_NOT_FOUND_EX_MSG_END = " is not found.";
    private static final String WRONG_NAME_EX_MSG = """
            Warning!\s
             The legal name consists of letters that could be separated by one space or hyphen.\s
             The name is not allowed here:\s""";
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

        handler.create(MOS_DAO, COUNTRY_ID);
        MOS_DAO.setCountryDAO(RUS);
        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(cityServiceRest).theSameCityExists(COUNTRY_ID, MOSCOW),
                () -> verify(countryServiceRest).getCapitalByCountryId(COUNTRY_ID),
                () -> verify(countryServiceRest).getCountryById(COUNTRY_ID),
                () -> verify(cityServiceRest).create(MOS_DAO),
                () -> verifyNoMoreInteractions(cityServiceRest, countryServiceRest)
        );
    }

    @Test
    void createCapitalCityWhenCapitalAlreadyExists() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS);
        when(countryServiceRest.getCapitalByCountryId(COUNTRY_ID)).thenReturn(MOS_DAO_FROM_REPO);

        CapitalAlreadyExistsException exception = assertThrows(CapitalAlreadyExistsException.class,
                () -> handler.create(MOS_DAO, COUNTRY_ID));

        assertAll(
                () -> assertEquals("The capital for the country ID " + COUNTRY_ID +
                        " is already set. Try to update this city.", exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(cityServiceRest).theSameCityExists(COUNTRY_ID, MOSCOW),
                () -> verify(countryServiceRest).getCapitalByCountryId(COUNTRY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest, countryServiceRest)
        );
    }

    @Test
    void createIdIsNotFoundFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> handler.create(MOS_DAO, COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void createCityAlreadyExistsFail() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS);
        when(cityServiceRest.theSameCityExists(COUNTRY_ID, MOSCOW)).thenReturn(true);
        CityAlreadyExistsException exception = assertThrows(CityAlreadyExistsException.class,
                () -> handler.create(MOS_DAO, COUNTRY_ID));

        assertAll(

                () -> assertEquals("The city " + MOSCOW + " already exists.",
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(cityServiceRest).theSameCityExists(COUNTRY_ID, MOSCOW),
                () -> verifyNoMoreInteractions(countryServiceRest, cityServiceRest)
        );
    }

    @Test
    void createWrongNameFail() {
        String wrong = "777Mo$cow!";
        CityDAO city = new CityDAO(wrong, RUS);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.create(city, COUNTRY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + wrong,
                        exception.getMessage()),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getCitySuccess() {
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);
        when(cityServiceRest.getCity(CITY_ID)).thenReturn(MOS_DAO);

        handler.getCity(CITY_ID);

        assertAll(
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
        String sochi = "sochi";
        String spb = "saint-Petersburg";
        Set<CityDAO> cities = Set.of(MOS_DAO, new CityDAO(sochi, RUS, false), new CityDAO(spb, RUS, false));
        when(cityServiceRest.getAllCities()).thenReturn(cities);

        Set<CityDAO> actual = cityServiceRest.getAllCities();

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> verify(cityServiceRest).getAllCities(),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void updateSuccess() {
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS);
        String newName = "nerezinovaya";
        CityDAO cityDAO = new CityDAO(CITY_ID, newName, true);

        handler.update(cityDAO, COUNTRY_ID);

        assertAll(
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).getCountryById(COUNTRY_ID),
                () -> verify(cityServiceRest).theSameCityExists(COUNTRY_ID, newName),
                () -> verify(countryServiceRest).getCapitalByCountryId(COUNTRY_ID),
                () -> verify(cityServiceRest).update(new CityDAO(CITY_ID, newName.toLowerCase(), RUS, true)),
                () -> verifyNoMoreInteractions(cityServiceRest, countryServiceRest)
        );
    }

    @Test
    void updateFailIdNotFound() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> handler.update(MOS_DAO_FROM_REPO, COUNTRY_ID));

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
        CityDAO toChange = new CityDAO(CITY_ID, newName, true);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.update(toChange, COUNTRY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + newName, exception.getMessage()),
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verifyNoMoreInteractions(cityServiceRest)
        );
    }

    @Test
    void updateExistingCityFail() {
        String newName = "saint-petersburg";
        CityDAO toChange = new CityDAO(CITY_ID, newName, RUS, false);
        when(cityServiceRest.cityIdExists(CITY_ID)).thenReturn(true);
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(cityServiceRest.theSameCityExists(COUNTRY_ID, newName)).thenReturn(true);

        CityAlreadyExistsException exception = assertThrows(CityAlreadyExistsException.class,
                () -> handler.update(toChange, COUNTRY_ID));

        assertAll(
                () -> assertEquals("The city " + newName.toLowerCase() + " already exists.",
                        exception.getMessage()),
                () -> verify(cityServiceRest).cityIdExists(CITY_ID),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(cityServiceRest).theSameCityExists(COUNTRY_ID, newName),
                () -> verifyNoMoreInteractions(cityServiceRest, countryServiceRest)
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
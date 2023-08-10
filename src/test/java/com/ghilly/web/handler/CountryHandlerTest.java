package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.Country;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryHandlerTest {
    private static final int COUNTRY_ID = 1;
    private static final String NAME = "Russia";
    private static final Country RUS = new Country(NAME);
    private static final CountryDAO RUS_DAO = new CountryDAO(NAME);
    private static final String COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN = "The country ID ";
    private static final String ID_NOT_FOUND_EX_MSG_END = " is not found.";
    private static final String WRONG_NAME_EX_MSG = "Warning! \n The legal name consists of letters that " +
            "could be separated by one space or hyphen. \n The name is not allowed here: ";
    private CountryHandler handler;
    private CountryServiceRest countryServiceRest;

    @BeforeEach
    void init() {
        countryServiceRest = mock(CountryServiceRest.class);
        handler = new CountryHandler(countryServiceRest);
    }

    @Test
    void createSuccess() {
        handler.create(RUS);

        assertAll(
                () -> verify(countryServiceRest).countryNameExists(NAME),
                () -> verify(countryServiceRest).create(RUS_DAO),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void createNameAlreadyExistsFail() {
        when(countryServiceRest.countryNameExists(NAME)).thenReturn(true);
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.create(RUS));

        assertAll(
                () -> assertEquals("The country name " + NAME + " already exists.",
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryNameExists(NAME),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void createWrongNameFail() {
        String wrong = "777Mo$cow!";
        Country country = new Country(wrong);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.create(country));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + wrong,
                        exception.getMessage()),
                () -> verifyNoInteractions(countryServiceRest)
        );
    }

    @Test
    void getSuccess() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS_DAO);

        CountryDAO countryDAO = handler.getCountryById(COUNTRY_ID);

        assertAll(
                () -> assertEquals(countryDAO.getName(), NAME),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).getCountryById(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getCountryIdNotFound() {
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> handler.getCountryById(COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        ex.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getAllCountries() {
        CountryDAO af = new CountryDAO("Afghanistan");
        CountryDAO fr = new CountryDAO("France");
        CountryDAO cn = new CountryDAO("China");
        List<CountryDAO> expected = List.of(af, fr, cn);
        when(countryServiceRest.getAllCountries()).thenReturn(expected);

        List<CountryDAO> actual = countryServiceRest.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(countryServiceRest).getAllCountries(),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void updateSuccess() {
        CountryDAO ussr = new CountryDAO(COUNTRY_ID, "USSR");
        CountryDAO countryDAO = new CountryDAO(COUNTRY_ID, NAME);
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(ussr);

        handler.update(new Country(NAME), COUNTRY_ID);
        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).countryNameExists(NAME),
                () -> verify(countryServiceRest).update(countryDAO),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void updateFailIdNotFound() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> handler.update(new Country(NAME), COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void updateWrongNewNameFail() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        String newName = "Ru$$i@";

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.update(new Country(newName), COUNTRY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + newName, exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }


    @Test
    void updateExistingNewNameFail() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.countryNameExists(NAME)).thenReturn(true);

        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.update(new Country(NAME), COUNTRY_ID));

        assertAll(
                () -> assertEquals("The country name " + NAME + " already exists.",
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).countryNameExists(NAME),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void deleteSuccess() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);

        handler.delete(COUNTRY_ID);

        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).delete(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void deleteFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> handler.delete(COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }
}
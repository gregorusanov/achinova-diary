package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.Country;
import com.ghilly.model.entity.CountryDAO;
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
    private CountryServiceRest service;

    @BeforeEach
    void init() {
        service = mock(CountryServiceRest.class);
        handler = new CountryHandler(service);
    }

    @Test
    void createSuccess() {
        handler.create(RUS);

        assertAll(
                () -> verify(service).countryNameExists(NAME),
                () -> verify(service).create(RUS_DAO),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void createNameAlreadyExistsFail() {
        when(service.countryNameExists(NAME)).thenReturn(true);
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.create(RUS));

        assertAll(
                () -> assertEquals("The country name " + NAME + " already exists.",
                        exception.getMessage()),
                () -> verify(service).countryNameExists(NAME),
                () -> verifyNoMoreInteractions(service)
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
                () -> verifyNoInteractions(service)
        );
    }

    @Test
    void getSuccess() {
        when(service.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(service.getCountryById(COUNTRY_ID)).thenReturn(RUS_DAO);

        CountryDAO countryDAO = handler.getCountryById(COUNTRY_ID);

        assertAll(
                () -> assertEquals(countryDAO.getName(), NAME),
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verify(service).getCountryById(COUNTRY_ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCountryIdNotFound() {
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> handler.getCountryById(COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        ex.getMessage()),
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getAllCountries() {
        CountryDAO af = new CountryDAO("Afghanistan");
        CountryDAO fr = new CountryDAO("France");
        CountryDAO cn = new CountryDAO("China");
        List<CountryDAO> expected = List.of(af, fr, cn);
        when(service.getAllCountries()).thenReturn(expected);

        List<CountryDAO> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(service).getAllCountries(),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateSuccess() {
        CountryDAO ussr = new CountryDAO(COUNTRY_ID, "USSR");
        CountryDAO countryDAO = new CountryDAO(COUNTRY_ID, NAME);
        when(service.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(service.getCountryById(COUNTRY_ID)).thenReturn(ussr);

        handler.update(new Country(NAME), COUNTRY_ID);
        assertAll(
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verify(service).countryNameExists(NAME),
                () -> verify(service).update(countryDAO),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateFailIdNotFound() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> handler.update(new Country(NAME), COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateWrongNewNameFail() {
        when(service.countryIdExists(COUNTRY_ID)).thenReturn(true);
        String newName = "Ru$$i@";

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> handler.update(new Country(newName), COUNTRY_ID));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + newName, exception.getMessage()),
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(service)
        );
    }


    @Test
    void updateExistingNewNameFail() {
        when(service.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(service.countryNameExists(NAME)).thenReturn(true);

        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> handler.update(new Country(NAME), COUNTRY_ID));

        assertAll(
                () -> assertEquals("The country name " + NAME + " already exists.",
                        exception.getMessage()),
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verify(service).countryNameExists(NAME),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void deleteSuccess() {
        when(service.countryIdExists(COUNTRY_ID)).thenReturn(true);

        handler.delete(COUNTRY_ID);

        assertAll(
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verify(service).delete(COUNTRY_ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void deleteFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> handler.delete(COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(service).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(service)
        );
    }
}
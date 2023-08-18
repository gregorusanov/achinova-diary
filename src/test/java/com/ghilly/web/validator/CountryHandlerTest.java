package com.ghilly.web.validator;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryHandlerTest {
    private static final int COUNTRY_ID = 1;
    private static final String NAME = "Russia";
    private static final CountryDAO RUS = new CountryDAO(COUNTRY_ID, NAME);
    private static final String COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN = "The country ID ";
    private static final String ID_NOT_FOUND_EX_MSG_END = " is not found.";
    private static final String WRONG_NAME_EX_MSG = """
            Warning!\s
             The legal name consists of letters that could be separated by one space or hyphen.\s
             The name is not allowed here:\s""";
    private CountryHandler countryHandler;
    private CountryServiceRest countryServiceRest;

    @BeforeEach
    void init() {
        countryServiceRest = mock(CountryServiceRest.class);
        countryHandler = new CountryHandler(countryServiceRest);
    }

    @Test
    void createSuccess() {
        countryHandler.create(RUS);

        assertAll(
                () -> verify(countryServiceRest).countryNameExists(NAME),
                () -> verify(countryServiceRest).create(RUS),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void createNameAlreadyExistsFail() {
        when(countryServiceRest.countryNameExists(NAME)).thenReturn(true);
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> countryHandler.create(RUS));

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
        CountryDAO country = new CountryDAO(wrong);

        WrongNameException exception = assertThrows(WrongNameException.class,
                () -> countryHandler.create(country));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + wrong,
                        exception.getMessage()),
                () -> verifyNoInteractions(countryServiceRest)
        );
    }

    @Test
    void getSuccess() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS);

        CountryDAO countryDAO = countryHandler.getCountryById(COUNTRY_ID);

        assertAll(
                () -> assertEquals(countryDAO.getName(), NAME),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).getCountryById(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getCountryIdNotFound() {
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> countryHandler.getCountryById(COUNTRY_ID));

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
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(ussr);

        countryHandler.update(RUS);
        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).countryNameExists(NAME),
                () -> verify(countryServiceRest).update(RUS),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void updateFailIdNotFound() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> countryHandler.update(RUS));

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
                () -> countryHandler.update(new CountryDAO(COUNTRY_ID, newName)));

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
                () -> countryHandler.update(RUS));

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

        countryHandler.delete(COUNTRY_ID);

        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).delete(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void deleteFail() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> countryHandler.delete(COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getAllCitiesForOneCountry() {
        String kyoto = "Kyoto";
        String tokyo = "Tokyo";
        CountryDAO countryDAO = new CountryDAO("Japan");
        int id = countryDAO.getId();
        List<CityDAO> cities = List.of(new CityDAO(kyoto, countryDAO, false), new CityDAO(tokyo, countryDAO, true));
        when(countryServiceRest.countryIdExists(id)).thenReturn(true);
        when(countryServiceRest.getAllCitiesByCountryId(id)).thenReturn(cities);

        List<CityDAO> actual = countryHandler.getAllCitiesByCountryId(id);

        assertAll(
                () -> assertEquals(2, actual.size()),
                () -> assertEquals(actual.get(0).getName(), kyoto),
                () -> assertEquals(actual.get(1).getName(), tokyo),
                () -> verify(countryServiceRest).getAllCitiesByCountryId(id),
                () -> verify(countryServiceRest).countryIdExists(id),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }
}
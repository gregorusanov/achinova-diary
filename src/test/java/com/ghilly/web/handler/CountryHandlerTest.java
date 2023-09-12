package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.service.CountryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryHandlerTest {
    private static final int COUNTRY_ID = 1;
    private static final String RUSSIA = "russia";
    private static final CountryEntity RUS_DAO = new CountryEntity(COUNTRY_ID, RUSSIA);
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
        countryHandler.create(RUS_DAO);

        assertAll(
                () -> verify(countryServiceRest).countryNameExists(RUSSIA),
                () -> verify(countryServiceRest).create(RUS_DAO),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void createNameAlreadyExistsFail() {
        when(countryServiceRest.countryNameExists(RUSSIA)).thenReturn(true);
        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> countryHandler.create(RUS_DAO));

        assertAll(
                () -> assertEquals("The country name " + RUSSIA + " already exists.",
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryNameExists(RUSSIA),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void createWrongNameFail() {
        String wrong = "777Mo$cow!";
        CountryEntity country = new CountryEntity(wrong);

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
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(RUS_DAO);

        CountryEntity countryEntity = countryHandler.getCountryById(COUNTRY_ID);

        assertAll(
                () -> assertEquals(RUSSIA, countryEntity.getName()),
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
        CountryEntity af = new CountryEntity("Afghanistan");
        CountryEntity fr = new CountryEntity("France");
        CountryEntity cn = new CountryEntity("China");
        Set<CountryEntity> expected = Set.of(af, fr, cn);
        when(countryServiceRest.getAllCountries()).thenReturn(expected);

        Set<CountryEntity> actual = countryServiceRest.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(countryServiceRest).getAllCountries(),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void updateSuccess() {
        CountryEntity ussr = new CountryEntity(COUNTRY_ID, "ussr");
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCountryById(COUNTRY_ID)).thenReturn(ussr);

        countryHandler.update(RUS_DAO);
        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).countryNameExists(RUSSIA),
                () -> verify(countryServiceRest).update(RUS_DAO),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void updateFailIdNotFound() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> countryHandler.update(RUS_DAO));

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
                () -> countryHandler.update(new CountryEntity(COUNTRY_ID, newName)));

        assertAll(
                () -> assertEquals(WRONG_NAME_EX_MSG + newName, exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }


    @Test
    void updateExistingNewNameFail() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.countryNameExists(RUSSIA)).thenReturn(true);

        NameAlreadyExistsException exception = assertThrows(NameAlreadyExistsException.class,
                () -> countryHandler.update(RUS_DAO));

        assertAll(
                () -> assertEquals("The country name " + RUSSIA + " already exists.",
                        exception.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).countryNameExists(RUSSIA),
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
        CountryEntity countryEntity = new CountryEntity("Japan");
        int id = countryEntity.getId();
        Set<CityEntity> cities = Set.of(new CityEntity(kyoto, countryEntity, false), new CityEntity(tokyo, countryEntity, true));
        when(countryServiceRest.countryIdExists(id)).thenReturn(true);
        when(countryServiceRest.getAllCitiesByCountryId(id)).thenReturn(cities);

        Set<CityEntity> actual = countryHandler.getAllCitiesByCountryId(id);

        assertAll(
                () -> assertEquals(2, actual.size()),
                () -> assertEquals(cities, actual),
                () -> verify(countryServiceRest).getAllCitiesByCountryId(id),
                () -> verify(countryServiceRest).countryIdExists(id),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getCapitalByCountryIdSuccess() {
        when(countryServiceRest.countryIdExists(COUNTRY_ID)).thenReturn(true);
        when(countryServiceRest.getCapitalByCountryId(COUNTRY_ID)).thenReturn(new CityEntity("Moscow", RUS_DAO, true));

        countryHandler.getCapitalByCountryId(COUNTRY_ID);

        assertAll(
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verify(countryServiceRest).getCapitalByCountryId(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }

    @Test
    void getCapitalByCountryIdIdNotFound() {
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> countryHandler.getCapitalByCountryId(COUNTRY_ID));

        assertAll(
                () -> assertEquals(COUNTRY_ID_NOT_FOUND_EX_MSG_BEGIN + COUNTRY_ID + ID_NOT_FOUND_EX_MSG_END,
                        ex.getMessage()),
                () -> verify(countryServiceRest).countryIdExists(COUNTRY_ID),
                () -> verifyNoMoreInteractions(countryServiceRest)
        );
    }
}
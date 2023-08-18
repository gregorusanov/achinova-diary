package com.ghilly.web.controller;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.web.validator.CountryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    private static final int ID = 100;
    private static final String NAME = "USSR";
    private static final com.ghilly.model.DTO.CountryDTO USSR_DTO = new com.ghilly.model.DTO.CountryDTO(NAME);
    private static final CountryDAO USSR_DAO = new CountryDAO(NAME);
    private static final CountryDAO USSR_DAO_FROM_REPO = new CountryDAO(ID, NAME);
    private CountryHandler countryHandler;
    private CountryController controller;

    @BeforeEach
    void init() {
        countryHandler = mock(CountryHandler.class);
        controller = new CountryController(countryHandler);
    }

    @Test
    void createCountry() {
        when(countryHandler.create(USSR_DAO)).thenReturn(USSR_DAO_FROM_REPO);
        controller.create(USSR_DTO);

        assertAll(
                () -> verify(countryHandler).create(USSR_DAO),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getCountries() {
        CountryDAO usa = new CountryDAO(2, "USA");
        List<CountryDAO> expected = List.of(USSR_DAO_FROM_REPO, usa);
        when(countryHandler.getAllCountries()).thenReturn(expected);

        controller.getAllCountries();

        assertAll(
                () -> verify(countryHandler).getAllCountries(),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getCountry() {
        when(countryHandler.getCountryById(ID)).thenReturn(USSR_DAO_FROM_REPO);

        controller.getCountryById(ID);

        assertAll(
                () -> verify(countryHandler).getCountryById(ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void updateCountry() {
        String newName = "Russia";
        com.ghilly.model.DTO.CountryDTO toChange = new com.ghilly.model.DTO.CountryDTO(newName);
        CountryDAO updated = new CountryDAO(ID, newName);
        when(countryHandler.update(updated)).thenReturn(updated);

        controller.update(toChange, ID);

        assertAll(
                () -> verify(countryHandler).update(updated),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void deleteCountry() {
        controller.deleteByCountryId(ID);

        assertAll(
                () -> verify(countryHandler).delete(ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getAllCitiesByCountryID() {
        String kyoto = "Kyoto";
        String tokyo = "Tokyo";
        CountryDAO japan = new CountryDAO("Japan");
        int id = japan.getId();
        List<CityDAO> cities = List.of(new CityDAO(kyoto, japan, false), new CityDAO(tokyo, japan, true));
        when(countryHandler.getAllCitiesByCountryId(id)).thenReturn(cities);

        controller.getAllCitiesByCountryId(id);

        assertAll(
                () -> verify(countryHandler).getAllCitiesByCountryId(id),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }
}
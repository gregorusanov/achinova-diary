package com.ghilly.web.controller;

import com.ghilly.model.Country;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.web.handler.CountryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    private static final int ID = 100;
    private static final String NAME = "USSR";
    private static final Country USSR = new Country(NAME);
    private static final CountryDAO USSR_DAO = new CountryDAO(ID, NAME);
    private CountryHandler countryHandler;
    private CountryController controller;

    @BeforeEach
    void init() {
        countryHandler = mock(CountryHandler.class);
        controller = new CountryController(countryHandler);
    }

    @Test
    void createCountry() {
        controller.create(USSR);

        assertAll(
                () -> verify(countryHandler).create(USSR),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getCountries() {
        CountryDAO usa = new CountryDAO(2, "USA");
        List<CountryDAO> expected = List.of(USSR_DAO, usa);
        when(countryHandler.getAllCountries()).thenReturn(expected);

        List<CountryDAO> actual = controller.getAllCountries().getBody();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(countryHandler).getAllCountries(),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getCountry() {
        when(countryHandler.getCountryById(ID)).thenReturn(USSR_DAO);

        CountryDAO actual = controller.getCountry(ID).getBody();

        assertAll(
                () -> assertEquals(USSR_DAO, actual),
                () -> verify(countryHandler).getCountryById(ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void updateCountry() {
        String newName = "Russia";
        Country toChange = new Country(newName);

        controller.update(toChange, ID);

        assertAll(
                () -> verify(countryHandler).update(toChange, ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void deleteCountry() {
        controller.delete(ID);

        assertAll(
                () -> verify(countryHandler).delete(ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getAllCitiesForOneCountry() {
        String kyoto = "Kyoto";
        String tokyo = "Tokyo";
        CountryDAO japan = new CountryDAO("Japan");
        int id = japan.getId();
        List<CityDAO> cities = List.of(new CityDAO(kyoto, japan, false), new CityDAO(tokyo, japan, true));
        when(countryHandler.getCitiesByCountryId(id)).thenReturn(cities);

        controller.getCitiesByCountryId(id);

        assertAll(
                () -> verify(countryHandler).getCitiesByCountryId(id),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }
}
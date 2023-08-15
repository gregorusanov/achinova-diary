package com.ghilly.web.controller;

import com.ghilly.model.City;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.web.handler.CityHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityControllerTest {
    private static final int COUNTRY_ID = 1;
    private static final int CITY_ID = 9;
    private static final String CITY_NAME = "Moscow";
    private static final CountryDAO RUS = new CountryDAO(COUNTRY_ID, "Russia");
    private static final CityDAO CITY_DAO = new CityDAO(CITY_NAME, RUS, true);
    private static final City CITY = new City(CITY_NAME);
    private CityHandler handler;
    private CityController controller;

    @BeforeEach
    void init() {
        handler = mock(CityHandler.class);
        controller = new CityController(handler);
    }

    @Test
    void createCity() {
        controller.create(CITY, COUNTRY_ID);

        assertAll(
                () -> verify(handler).create(CITY, COUNTRY_ID),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void getCity() {
        when(handler.getCity(CITY_ID)).thenReturn(CITY_DAO);

        CityDAO actual = controller.getCity(CITY_ID).getBody();

        assertAll(
                () -> {
                    assert actual != null;
                    assertEquals(CITY_NAME, actual.getName());
                },
                () -> verify(handler).getCity(CITY_ID),
                () -> verifyNoMoreInteractions(handler)
        );

    }

    @Test
    void getAllCities() {
        String sochi = "Sochi";
        String spb = "Saint-Petersburg";
        boolean notCapital = false;
        List<CityDAO> cities = List.of(CITY_DAO, new CityDAO(spb, RUS, notCapital), new CityDAO(sochi, RUS, notCapital));
        when(handler.getAllCities()).thenReturn(cities);

        controller.getAllCities();

        assertAll(
                () -> verify(handler).getAllCities(),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void update() {
        String newName = "Moskvabad";
        City city = new City(CITY_ID, newName);

        controller.update(city, COUNTRY_ID);

        assertAll(
                () -> verify(handler).update(city, COUNTRY_ID),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void delete() {
        controller.deleteCity(CITY_ID);

        assertAll(
                () -> verify(handler).delete(CITY_ID),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void getAllCitiesForOneCountry() {
        String kyoto = "Kyoto";
        String tokyo = "Tokyo";
        CountryDAO japan = new CountryDAO("Japan");
        int id = japan.getId();
        List<CityDAO> cities = List.of(new CityDAO(kyoto, japan, false), new CityDAO(tokyo, japan, true));
        when(handler.getAllCitiesForOneCountry(id)).thenReturn(cities);

        handler.getAllCitiesForOneCountry(id);

        assertAll(
                () -> verify(handler).getAllCitiesForOneCountry(id),
                () -> verifyNoMoreInteractions(handler)
        );
    }
}
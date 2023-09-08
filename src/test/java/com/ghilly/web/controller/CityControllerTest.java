package com.ghilly.web.controller;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.model.DTO.CityDTO;
import com.ghilly.web.handler.CityHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityControllerTest {
    private static final int COUNTRY_ID = 1;
    private static final int CITY_ID = 9;
    private static final String CITY_NAME = "moscow";
    private static final CountryDAO RUS = new CountryDAO(COUNTRY_ID, "russia");
    private static final CityDAO CITY_DAO_FROM_REPO = new CityDAO(CITY_ID, CITY_NAME, RUS, true);
    private static final CityDTO CITY_DTO = new CityDTO(CITY_NAME.toUpperCase(), COUNTRY_ID, true);
    private static final CityDAO CITY_DAO = new CityDAO(CITY_NAME, null, true);
    private CityHandler handler;
    private CityController controller;

    @BeforeEach
    void init() {
        handler = mock(CityHandler.class);
        controller = new CityController(handler);
    }

    @Test
    void createCity() {
        when(handler.create(CITY_DAO, COUNTRY_ID)).thenReturn(CITY_DAO_FROM_REPO);

        CityDTO actual = controller.create(CITY_DTO).getBody();

        assertAll(
                () -> {
                    assert actual != null;
                    assertEquals(actual.getCountryId(), COUNTRY_ID);
                },
                () -> verify(handler).create(CITY_DAO, COUNTRY_ID),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void getCity() {
        when(handler.getCity(CITY_ID)).thenReturn(CITY_DAO_FROM_REPO);

        CityDTO actual = controller.getCity(CITY_ID).getBody();

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
        Set<CityDAO> cities = Set.of(CITY_DAO_FROM_REPO, new CityDAO(spb, RUS, notCapital), new CityDAO(sochi, RUS, notCapital));
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
        CityDTO city = new CityDTO(newName, COUNTRY_ID);
        CityDAO transformedCity = new CityDAO(CITY_ID, newName.toLowerCase());

        controller.update(city, CITY_ID);

        assertAll(
                () -> verify(handler).update(transformedCity, COUNTRY_ID),
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
}
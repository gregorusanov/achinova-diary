package com.ghilly.web.controller;

import com.ghilly.model.City;
import com.ghilly.model.Country;
import com.ghilly.service.CityServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityControllerTest {
    private static final int ID = 1;
    private static final String CITY_NAME = "Moscow";
    private static final Country RUS = new Country(ID, "Russia");
    private static final City CITY = new City(CITY_NAME, RUS, true);
    private CityServiceRest service;
    private CityController controller;

    @BeforeEach
    void init() {
        service = mock(CityServiceRest.class);
        controller = new CityController(service);
    }

    @Test
    void createCity() {
        controller.create(CITY);

        assertAll(
                () -> verify(service).create(CITY),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void getCity() {
        when(service.getCity(ID)).thenReturn(CITY);

        City actual = controller.getCity(ID).getBody();

        assertAll(
                () -> assertEquals(CITY_NAME, actual.getName()),
                () -> verify(service).getCity(ID),
                () -> verifyNoMoreInteractions(service)
        );

    }

    @Test
    void getAllCities() {
        String sochi = "Sochi";
        String spb = "Saint-Petersburg";
        boolean notCapital = false;
        List<City> cities = List.of(CITY, new City(sochi, RUS, notCapital), new City(spb, RUS, notCapital));
        when(service.getAllCities()).thenReturn(cities);

        controller.getAllCities();

        assertAll(
                () -> verify(service).getAllCities(),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void updateSuccess() {
        String newName = "Moskvabad";
        City toChange = new City(ID, newName, RUS, true);

        controller.update(toChange);

        assertAll(
                () -> verify(service).update(toChange),
                () -> verify(service).getCity(ID),
                () -> verifyNoMoreInteractions(service)
        );
    }

    @Test
    void deleteSuccess() {
        controller.deleteCity(ID);

        assertAll(
                () -> verify(service).delete(ID),
                () -> verifyNoMoreInteractions(service)
        );
    }
}
package com.ghilly.web.controller;

import com.ghilly.model.City;
import com.ghilly.service.CityServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityControllerTest {
    private static final int ID = 1;
    private static final String CITY_NAME = "Moscow";
    private static final City CITY = new City(CITY_NAME, ID);
    private CityServiceRest service;
    private CityController controller;

    @BeforeEach
    void init() {
        service = mock(CityServiceRest.class);
        controller = new CityController(service);
    }

    @Test
    void createCity() {
        controller.create(CITY_NAME, ID);

        assertAll(
                () -> verify(service).create(CITY_NAME, ID),
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

}
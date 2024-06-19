package com.ghilly.web.controller;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dto.City;
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
    private static final CountryEntity RUS = new CountryEntity(COUNTRY_ID, "russia");
    private static final CityEntity CITY_DAO_FROM_REPO = CityEntity.builder().id(CITY_ID).name(CITY_NAME)
            .countryEntity(RUS).capital(true).build();
    private static final City CITY_DTO = City.builder().name(CITY_NAME.toUpperCase()).countryId(COUNTRY_ID)
            .capital(true).build();
    private static final CityEntity CITY_DAO = CityEntity.builder().name(CITY_NAME).countryEntity(null).capital(true)
            .build();
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

        City actual = controller.create(CITY_DTO).getBody();

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

        City actual = controller.getCity(CITY_ID).getBody();

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
        Set<CityEntity> cities = Set.of(CITY_DAO_FROM_REPO,
                CityEntity.builder().name(spb).countryEntity(RUS).build(),
                CityEntity.builder().name(sochi).countryEntity(RUS).build());
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
        City city = City.builder().name(newName).countryId(COUNTRY_ID).build();
        CityEntity transformedCity = CityEntity.builder().id(CITY_ID).name(newName.toLowerCase()).build();

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
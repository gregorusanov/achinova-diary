package com.ghilly.service;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityServiceRestTest {
    private static final String MOSCOW = "Moscow";
    private static final int COUNTRY_ID = 1;
    private static final int CITY_ID = 7;
    private static final CountryEntity RUS = new CountryEntity(COUNTRY_ID, "Russia");
    private static final CityEntity MOS = new CityEntity(CITY_ID, MOSCOW, RUS, true);
    private CityServiceRest service;
    private CityRepository cityRepository;

    @BeforeEach
    void init() {
        cityRepository = mock(CityRepository.class);
        service = new CityServiceRest(cityRepository);
    }

    @Test
    void create() {
        when(cityRepository.save(MOS)).thenReturn(MOS);
        service.create(MOS);

        assertAll(
                () -> verify(cityRepository).save(MOS),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }


    @Test
    void getCity() {
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS));

        CityEntity cityEntity = service.getCity(CITY_ID);

        assertAll(
                () -> assertEquals(cityEntity.getName(), MOSCOW),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void getAllCities() {
        String berlin = "Berlin";
        String paris = "Paris";
        Set<CityEntity> cities = Set.of(new CityEntity(berlin, new CountryEntity("Germany"), true), MOS,
                new CityEntity(paris, new CountryEntity("France"), true));
        when(cityRepository.findAll()).thenReturn(cities);

        Set<CityEntity> actual = service.getAllCities();

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> verify(cityRepository).findAll(),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void update() {
        String name = "moskvabad";
        CityEntity toChange = new CityEntity(CITY_ID, name, RUS, true);
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(toChange));

        service.update(toChange);

        assertAll(
                () -> verify(cityRepository).save(toChange),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void deleteSuccess() {
        service.delete(CITY_ID);

        assertAll(
                () -> verify(cityRepository).deleteById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }
}
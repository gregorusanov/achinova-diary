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
    private static final CityEntity MOS = CityEntity.builder().id(CITY_ID).name(MOSCOW).countryEntity(RUS)
            .capital(true).build();
    private CityServiceRest service;
    private CityRepository cityRepository;

    @BeforeEach
    void init() {
        cityRepository = mock(CityRepository.class);
        service = new CityServiceRest(cityRepository);
        MOS.setName(MOSCOW);
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
        Set<CityEntity> cities = Set.of(
                CityEntity.builder().name(berlin).countryEntity(new CountryEntity("Germany")).capital(true).build(),
                MOS,
                CityEntity.builder().name(paris).countryEntity(new CountryEntity("France")).capital(true).build());
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
        MOS.setName(name);
        when(cityRepository.findById(CITY_ID)).thenReturn(Optional.of(MOS));

        service.update(MOS);

        assertAll(
                () -> verify(cityRepository).save(MOS),
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
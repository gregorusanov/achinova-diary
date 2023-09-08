package com.ghilly.service;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
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
    private static final CountryDAO RUS = new CountryDAO(COUNTRY_ID, "Russia");
    private static final CityDAO MOS = new CityDAO(CITY_ID, MOSCOW, RUS, true);
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

        CityDAO cityDAO = service.getCity(CITY_ID);

        assertAll(
                () -> assertEquals(cityDAO.getName(), MOSCOW),
                () -> verify(cityRepository).findById(CITY_ID),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void getAllCities() {
        String berlin = "Berlin";
        String paris = "Paris";
        Set<CityDAO> cities = Set.of(new CityDAO(berlin, new CountryDAO("Germany"), true), MOS,
                new CityDAO(paris, new CountryDAO("France"), true));
        when(cityRepository.findAll()).thenReturn(cities);

        Set<CityDAO> actual = service.getAllCities();

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> verify(cityRepository).findAll(),
                () -> verifyNoMoreInteractions(cityRepository)
        );
    }

    @Test
    void update() {
        String name = "moskvabad";
        CityDAO toChange = new CityDAO(CITY_ID, name, RUS, true);
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
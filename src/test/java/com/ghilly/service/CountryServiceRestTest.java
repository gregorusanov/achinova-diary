package com.ghilly.service;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CountryServiceRestTest {
    private static final String NAME = "Turkey";
    private static final int ID = 1;
    private static final CountryEntity COUNTRY_DAO = new CountryEntity(ID, NAME);
    private CountryRepository countryRepository;
    private CityRepository cityRepository;
    private CountryServiceRest service;

    @BeforeEach
    void init() {
        countryRepository = mock(CountryRepository.class);
        cityRepository = mock(CityRepository.class);
        service = new CountryServiceRest(countryRepository, cityRepository);
    }

    @Test
    void createCountry() {
        when(countryRepository.save(COUNTRY_DAO)).thenReturn(COUNTRY_DAO);
        service.create(COUNTRY_DAO);

        assertAll(
                () -> verify(countryRepository).save(COUNTRY_DAO),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }


    @Test
    void getAllCountries() {
        CountryEntity af = new CountryEntity("Afghanistan");
        CountryEntity fr = new CountryEntity("France");
        CountryEntity cn = new CountryEntity("China");
        Set<CountryEntity> expected = Set.of(af, fr, cn);
        when(countryRepository.findAll()).thenReturn(expected);

        Set<CountryEntity> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(countryRepository).findAll(),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCountry() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(COUNTRY_DAO));

        CountryEntity expected = service.getCountryById(ID);

        assertAll(
                () -> assertEquals(expected.getName(), NAME),
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void update() {
        String newName = "Russia";
        CountryEntity countryEntity = new CountryEntity(ID, newName);
        when(countryRepository.save(countryEntity)).thenReturn(countryEntity);

        service.update(countryEntity);

        assertAll(
                () -> verify(countryRepository).save(countryEntity),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void delete() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(COUNTRY_DAO));

        service.delete(ID);

        assertAll(
                () -> verify(countryRepository).deleteById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getAllCitiesByCountry() {
        CountryEntity countryEntity = new CountryEntity("Germany");
        int id = countryEntity.getId();
        CityEntity br = CityEntity.builder().name("Berlin").countryEntity(countryEntity).capital(true).build();
        CityEntity munich = CityEntity.builder().name("Munich").countryEntity(countryEntity).build();
        CityEntity dresden = CityEntity.builder().name("Dresden").countryEntity(countryEntity).build();
        countryEntity.setCitySet(Set.of(br, munich, dresden));
        when(countryRepository.findById(id)).thenReturn(Optional.of(countryEntity));

        Set<CityEntity> actual = service.getAllCitiesByCountryId(id);

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> verify(countryRepository).findById(id),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCapitalByCountryId() {
        when(cityRepository.findCityEntityByCountryEntity_IdAndCapitalIsTrue(ID))
                .thenReturn(Optional.of(CityEntity.builder().name("Istanbul").countryEntity(COUNTRY_DAO).capital(true)
                        .build()));

        service.getCapitalByCountryId(ID);

        assertAll(
                () -> verify(cityRepository).findCityEntityByCountryEntity_IdAndCapitalIsTrue(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }
}
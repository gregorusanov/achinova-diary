package com.ghilly.web.controller;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dto.Country;
import com.ghilly.web.handler.CountryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    private static final int ID = 100;
    private static final String NAME = "ussr";
    private static final Country USSR_DTO = new Country(NAME.toUpperCase());
    private static final CountryEntity USSR_DAO = new CountryEntity(NAME);
    private static final CountryEntity USSR_DAO_FROM_REPO = new CountryEntity(ID, NAME);
    private CountryHandler countryHandler;
    private CountryController controller;

    @BeforeEach
    void init() {
        countryHandler = mock(CountryHandler.class);
        controller = new CountryController(countryHandler);
    }

    @Test
    void createCountry() {
        when(countryHandler.create(USSR_DAO)).thenReturn(USSR_DAO_FROM_REPO);
        controller.create(USSR_DTO);

        assertAll(
                () -> verify(countryHandler).create(USSR_DAO),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getCountries() {
        CountryEntity usa = new CountryEntity(2, "usa");
        Set<CountryEntity> expected = Set.of(USSR_DAO_FROM_REPO, usa);
        when(countryHandler.getAllCountries()).thenReturn(expected);

        controller.getAllCountries();

        assertAll(
                () -> verify(countryHandler).getAllCountries(),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getCountry() {
        when(countryHandler.getCountryById(ID)).thenReturn(USSR_DAO_FROM_REPO);

        controller.getCountryById(ID);

        assertAll(
                () -> verify(countryHandler).getCountryById(ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void updateCountry() {
        String newName = "Russia";
        Country toChange = new Country(newName);
        CountryEntity updated = new CountryEntity(ID, newName.toLowerCase());
        when(countryHandler.update(updated)).thenReturn(updated);

        controller.update(toChange, ID);

        assertAll(
                () -> verify(countryHandler).update(updated),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void deleteCountry() {
        controller.deleteByCountryId(ID);

        assertAll(
                () -> verify(countryHandler).delete(ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getAllCitiesByCountryID() {
        String kyoto = "kyoto";
        String tokyo = "tokyo";
        CountryEntity japan = new CountryEntity("japan");
        int id = 400;
        Set<CityEntity> cities = Set.of(CityEntity.builder().name(kyoto).countryEntity(japan).build(),
                CityEntity.builder().name(tokyo).countryEntity(japan).capital(true).build());
        when(countryHandler.getAllCitiesByCountryId(id)).thenReturn(cities);

        controller.getAllCitiesByCountryId(id);

        assertAll(
                () -> verify(countryHandler).getAllCitiesByCountryId(id),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }

    @Test
    void getCapitalByCountryId() {
        when(countryHandler.getCapitalByCountryId(ID)).thenReturn(
                CityEntity.builder().name("moscow").countryEntity(USSR_DAO_FROM_REPO).capital(true).build());

        controller.getCapitalByCountryId(ID);

        assertAll(
                () -> verify(countryHandler).getCapitalByCountryId(ID),
                () -> verifyNoMoreInteractions(countryHandler)
        );
    }
}
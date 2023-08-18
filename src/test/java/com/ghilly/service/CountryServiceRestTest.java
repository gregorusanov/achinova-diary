package com.ghilly.service;

import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CountryServiceRestTest {
    private static final String NAME = "Turkey";
    private static final int ID = 1;
    private static final CountryDAO COUNTRY_DAO = new CountryDAO(ID, NAME);
    private CountryRepository repository;
    private CountryServiceRest service;

    @BeforeEach
    void init() {
        repository = mock(CountryRepository.class);
        service = new CountryServiceRest(repository);
    }

    @Test
    void createCountry() {
        when(repository.save(COUNTRY_DAO)).thenReturn(COUNTRY_DAO);
        service.create(COUNTRY_DAO);

        assertAll(
                () -> verify(repository).save(COUNTRY_DAO),
                () -> verifyNoMoreInteractions(repository)
        );
    }


    @Test
    void getAllCountries() {
        CountryDAO af = new CountryDAO("Afghanistan");
        CountryDAO fr = new CountryDAO("France");
        CountryDAO cn = new CountryDAO("China");
        List<CountryDAO> expected = List.of(af, fr, cn);
        when(repository.findAll()).thenReturn(expected);

        List<CountryDAO> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(repository).findAll(),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void getCountry() {
        when(repository.findById(ID)).thenReturn(Optional.of(COUNTRY_DAO));

        CountryDAO expected = service.getCountryById(ID);

        assertAll(
                () -> assertEquals(expected.getName(), NAME),
                () -> verify(repository).findById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void update() {
        String newName = "Russia";
        CountryDAO countryDAO = new CountryDAO(ID, newName);
        when(repository.save(countryDAO)).thenReturn(countryDAO);

        service.update(countryDAO);

        assertAll(
                () -> verify(repository).save(countryDAO),
                () -> verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void delete() {
        when(repository.findById(ID)).thenReturn(Optional.of(COUNTRY_DAO));

        service.delete(ID);

        assertAll(
                () -> verify(repository).deleteById(ID),
                () -> verifyNoMoreInteractions(repository)
        );
    }
}
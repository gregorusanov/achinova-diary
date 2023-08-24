package com.ghilly.service;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceRestTest {
    private static final String NAME = "Turkey";
    private static final int ID = 1;
    private static final CountryDAO COUNTRY_DAO = new CountryDAO(ID, NAME);
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
        CountryDAO af = new CountryDAO("Afghanistan");
        CountryDAO fr = new CountryDAO("France");
        CountryDAO cn = new CountryDAO("China");
        List<CountryDAO> expected = List.of(af, fr, cn);
        when(countryRepository.findAll()).thenReturn(expected);

        List<CountryDAO> actual = service.getAllCountries();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verify(countryRepository).findAll(),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCountry() {
        when(countryRepository.findById(ID)).thenReturn(Optional.of(COUNTRY_DAO));

        CountryDAO expected = service.getCountryById(ID);

        assertAll(
                () -> assertEquals(expected.getName(), NAME),
                () -> verify(countryRepository).findById(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void update() {
        String newName = "Russia";
        CountryDAO countryDAO = new CountryDAO(ID, newName);
        when(countryRepository.save(countryDAO)).thenReturn(countryDAO);

        service.update(countryDAO);

        assertAll(
                () -> verify(countryRepository).save(countryDAO),
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
        CountryDAO countryDAO = new CountryDAO("Germany");
        int id = countryDAO.getId();
        CityDAO br = new CityDAO("Berlin", countryDAO, true);
        CityDAO munich = new CityDAO("Munich", countryDAO);
        CityDAO dresden = new CityDAO("Dresden", countryDAO);
        countryDAO.setCityList(List.of(br, munich, dresden));
        when(countryRepository.findById(id)).thenReturn(Optional.of(countryDAO));

        List <CityDAO> actual = service.getAllCitiesByCountryId(id);

        assertAll(
                () -> assertEquals(3, actual.size()),
                () -> verify(countryRepository).findById(id),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }

    @Test
    void getCapitalByCountryId() {
        when(cityRepository.findCityDAOByCountryDAOIdAndCapitalIsTrue(ID))
                .thenReturn(Optional.of(new CityDAO("Istanbul", COUNTRY_DAO, true)));

        service.getCapitalByCountryId(ID);

        assertAll(
                () -> verify(cityRepository).findCityDAOByCountryDAOIdAndCapitalIsTrue(ID),
                () -> verifyNoMoreInteractions(countryRepository)
        );
    }
}
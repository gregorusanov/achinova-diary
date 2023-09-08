package com.ghilly.service;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;

import java.util.Set;

public interface CountryService {
    CountryDAO create(CountryDAO countryDAO);

    Set<CountryDAO> getAllCountries();

    CountryDAO getCountryById(int countryId);

    CountryDAO update(CountryDAO countryDAO);

    void delete(int countryId);

    Set<CityDAO> getAllCitiesByCountryId(int countryId);

    CityDAO getCapitalByCountryId(int countryId);
}

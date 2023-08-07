package com.ghilly.service;

import com.ghilly.model.entity.CountryDAO;

import java.util.List;

public interface CountryService {
    CountryDAO create(CountryDAO countryDAO);

    List<CountryDAO> getAllCountries();

    CountryDAO getCountryById(int countryId);

    CountryDAO update(CountryDAO countryDAO);

    void delete(int countryId);
}

package com.ghilly.service;

import com.ghilly.model.Country;

import java.util.List;

public interface CountryService {
    Country create(Country country);

    List<Country> getAllCountries();

    Country getCountryById(int countryId);

    void update(Country country);

    void delete(int countryId);

    Country getCountryByCityId(int countryId);
}

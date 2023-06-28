package com.ghilly.service;

import com.ghilly.model.Country;

import java.util.List;

public interface CountryService {
    void add(String countryName);

    List<Country> getAllCountries();

    Country getCountry(int countryId);

    void upgrade(Country country);

    void remove(int countryId);
}

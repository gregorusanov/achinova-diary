package com.ghilly.service;

import com.ghilly.classes.Country;

import java.util.List;

public interface CountryService {
    void add(String countryName);

    List<Country> getAllCountries();

    Country getCountry(int countryId);

    void upgrade(int countryId, String newName);

    void remove(int countryId);
}

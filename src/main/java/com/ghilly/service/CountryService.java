package com.ghilly.service;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.model.Country;

import java.util.List;
import java.util.regex.Pattern;

public interface CountryService {
    Country create(String countryName);

    List<Country> getAllCountries();

    Country getCountryById(int countryId);

    void update(Country country);

    void delete(int countryId);
}

package com.ghilly.service;

import com.ghilly.model.Countries;

import java.util.List;

public interface CountryService {
    Countries create(String countryName);

    List<Countries> getAllCountries();

    Countries getCountryById(int countryId);

    void update(Countries countries);

    void delete(int countryId);
}

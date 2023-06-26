package com.ghilly.repository;

import com.ghilly.classes.Country;

import java.util.List;

public interface CountryRepository {
    void insert(String countryName);

    List<Country> takeAllCountries();

    Country takeCountry(int countryId);

    void update(int countryId, String newName);

    void delete(int countryId);

    boolean containsCountry(int countryId);


}

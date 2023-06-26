package com.ghilly.repository;


import com.ghilly.classes.Country;

import java.util.List;

public class CountryRepositoryRest implements CountryRepository {

    @Override
    public void insert(String countryName) {

    }

    @Override
    public List<Country> takeAllCountries() {
        return null;
    }

    @Override
    public Country takeCountry(int countryId) {
        return null;
    }

    @Override
    public void update(int countryId, String newName) {

    }

    @Override
    public void delete(int countryId) {

    }

    @Override
    public boolean containsCountry(int countryId) {
        return false;
    }
}

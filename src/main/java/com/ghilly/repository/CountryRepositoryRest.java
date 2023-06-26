package com.ghilly.repository;


import java.util.List;

public class CountryRepositoryRest implements CountryRepository {

    @Override
    public void insert(String countryName) {

    }

    @Override
    public List<String> takeAllCountries() {
        return null;
    }

    @Override
    public String takeCountry(int countryId) {
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

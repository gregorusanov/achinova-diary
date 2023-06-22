package com.ghilly.repository;


import java.util.List;
import java.util.Map;

public class CountryRepositoryRest implements CountryRepository {

    private CountryRepositoryRest countryRepository;

    @Override
    public void insert(String countryName) {

    }

    @Override
    public List<List> takeList() {
        return null;
    }

    @Override
    public String takeCountry(int countryId) {
        return null;
    }

    @Override
    public void change(int countryId, String newName) {

    }

    @Override
    public void remove(int countryId) {

    }

    @Override
    public boolean containsCountry(int countryId) {
        return false;
    }
}

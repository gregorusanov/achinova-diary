package com.ghilly.service;

import java.util.List;

public interface CountryService {
    void add(String countryName);

    List<String> getAllCountries();

    String getCountry(int countryId);

    void upgrade(int countryId, String newName);

    void remove(int countryId);
}

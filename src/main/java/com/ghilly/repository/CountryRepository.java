package com.ghilly.repository;

import java.util.List;

public interface CountryRepository {
    void insert(String countryName);

    List<String> takeAllCountries();

    String takeCountry(int countryId);

    void update(int countryId, String newName);

    void delete(int countryId);

    boolean containsCountry(int countryId);


}

package com.ghilly.repository;

import java.util.List;

public interface CountryRepository {
    void insert(String countryName);

    List<String> takeAllCountries();

    String takeCountry(int countryId);

    void change(int countryId, String newName);

    void cut(int countryId);

    boolean containsCountry(int countryId);


}

package com.ghilly.repository;

import java.util.List;

public interface CountryRepository {
    void insert(String countryName);

    List<List> takeList();

    String takeCountry(int countryId);

    void change(int countryId, String newName);

    void remove(int countryId);

    boolean containsCountry(int countryId);


}

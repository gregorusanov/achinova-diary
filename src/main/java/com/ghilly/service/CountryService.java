package com.ghilly.service;


import java.util.List;

public interface CountryService {
        String add(String countryName);

        List<List> receiveList();

        String receiveCountry(int countryId);

        String upgrade(int id, String newName);

        String clear(int id);
}

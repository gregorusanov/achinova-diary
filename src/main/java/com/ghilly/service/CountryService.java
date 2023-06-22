package com.ghilly.service;


import java.util.List;

public interface CountryService {
        String add(String countryName);

        List<String> receiveList();

        String receiveCountry(int countryId);

        String upgrade(int id, String newName);

        String remove(int id);
}

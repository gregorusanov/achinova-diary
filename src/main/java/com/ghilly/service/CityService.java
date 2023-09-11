package com.ghilly.service;

import com.ghilly.model.dao.CityDAO;

import java.util.Set;

public interface CityService {
    CityDAO create(CityDAO cityDAO);

    CityDAO getCity(int cityId);

    Set<CityDAO> getAllCities();

    CityDAO update(CityDAO cityDAO);

    void delete(int cityId);

    boolean theSameCityExists(int countryId, String name);
}

package com.ghilly.service;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;

import java.util.List;

public interface CityService {
    CityDAO create(CityDAO cityDAO);

    CityDAO getCity(int cityId);

    List<CityDAO> getAllCities();

    CityDAO update(CityDAO cityDAO);

    void delete(int cityId);

    boolean theSameCityExists(int countryId, String name);
}

package com.ghilly.service;

import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;

import java.util.List;

public interface CityService {
    CityDAO create(CityDAO cityDAO);

    CityDAO getCity(int cityId);

    List<CityDAO> getAllCities();

    CityDAO update(CityDAO cityDAO);

    void delete(int cityId);

    List<CityDAO> getCitiesByCountry(CountryDAO countryDAO);
}

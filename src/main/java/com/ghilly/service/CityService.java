package com.ghilly.service;

import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;

import java.util.List;

public interface CityService {
    CityDAO create(CityDAO cityDAO);

    CityDAO getCity(int cityId);

    List<CityDAO> getAllCities();

    CityDAO update(CityDAO cityDAO);

    CountryDAO getCountryByCityId(int cityId);

    void delete(int cityId);
}

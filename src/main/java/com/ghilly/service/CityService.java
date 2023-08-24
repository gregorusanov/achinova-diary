package com.ghilly.service;

import com.ghilly.model.DAO.CityDAO;

import java.util.List;

public interface CityService {
    CityDAO create(CityDAO cityDAO);

    CityDAO getCity(int cityId);

    List<CityDAO> getAllCities();

    CityDAO update(CityDAO cityDAO);

    void delete(int cityId);
}

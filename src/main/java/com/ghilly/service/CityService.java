package com.ghilly.service;

import com.ghilly.model.dao.CityEntity;

import java.util.Set;

public interface CityService {
    CityEntity create(CityEntity cityEntity);

    CityEntity getCity(int cityId);

    Set<CityEntity> getAllCities();

    CityEntity update(CityEntity cityEntity);

    void delete(int cityId);

    boolean theSameCityExists(int countryId, String name);
}

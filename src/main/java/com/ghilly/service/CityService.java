package com.ghilly.service;

import com.ghilly.model.City;

import java.util.List;

public interface CityService {
    City create(City city);

    City getCity(int cityId);

    List<City> getAllCities();

    void update(City city);

    void delete(int cityId);
}

package com.ghilly.service;

import com.ghilly.model.City;

import java.util.List;

public interface CityService {
    City create(String cityName, int countryId);

    City getCity(int cityId);

    List<City> getAllCities();
}

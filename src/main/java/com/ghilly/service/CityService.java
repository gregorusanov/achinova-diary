package com.ghilly.service;

import com.ghilly.model.City;

public interface CityService {
    City create(String cityName, int countryId);

    City getCity(int cityId);
}

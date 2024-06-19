package com.ghilly.service;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;

import java.util.Set;

public interface CountryService {
    CountryEntity create(CountryEntity countryEntity);

    Set<CountryEntity> getAllCountries();

    CountryEntity getCountryById(int countryId);

    CountryEntity update(CountryEntity countryEntity);

    void delete(int countryId);

    Set<CityEntity> getAllCitiesByCountryId(int countryId);

    CityEntity getCapitalByCountryId(int countryId);
}

package com.ghilly.service;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CityServiceRest implements CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceRest.class);
    private final CityRepository cityRepository;

    public CityServiceRest(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityEntity create(CityEntity cityEntity) {
        CityEntity toReturn = cityRepository.save(cityEntity);
        logger.info("The city {} is created, the country is {}", toReturn.getName(), toReturn.getCountryEntity().getName());
        return toReturn;
    }

    @Override
    public CityEntity getCity(int cityId) {
        logger.info("The city with the ID {} is found.", cityId);
        return cityRepository.findById(cityId).orElseThrow();
    }

    @Override
    public Set<CityEntity> getAllCities() {
        Set<CityEntity> cities = cityRepository.findAll();
        logger.info("The list of cities is: {}", cities);
        return cities;
    }

    @Override
    public CityEntity update(CityEntity cityEntity) {
        int id = cityEntity.getId();
        cityRepository.save(cityEntity);
        logger.info("The city with the ID {} is updated, new name is {}.", id, cityEntity.getName());
        return cityRepository.findById(id).orElseThrow();
    }

    @Override
    public void delete(int cityId) {
        cityRepository.deleteById(cityId);
        logger.info("The city with the ID {} is deleted", cityId);
    }

    @Override
    public boolean theSameCityExists(int countryId, String name) {
        return cityRepository.existsByCountryEntity_IdAndName(countryId, name);
    }

    public boolean cityIdExists(int id) {
        return cityRepository.findById(id).isPresent();
    }
}

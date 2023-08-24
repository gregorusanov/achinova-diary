package com.ghilly.service;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CityServiceRest implements CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceRest.class);
    private final CityRepository cityRepository;

    public CityServiceRest(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public CityDAO create(CityDAO cityDAO) {
        CityDAO toReturn = cityRepository.save(cityDAO);
        logger.info("The city {} is created, the country is {}", toReturn.getName(), toReturn.getCountry().getName());
        return toReturn;
    }

    @Override
    public CityDAO getCity(int cityId) {
        logger.info("The city with the ID {} is found.", cityId);
        return cityRepository.findById(cityId).orElseThrow();
    }

    @Override
    public List<CityDAO> getAllCities() {
        List<CityDAO> cities = (List<CityDAO>) cityRepository.findAll();
        logger.info("The list of cities is: {}", cities);
        return cities;
    }

    @Override
    public CityDAO update(CityDAO cityDAO) {
        int id = cityDAO.getId();
        cityRepository.save(cityDAO);
        logger.info("The city with the ID {} is updated, new name is {}.", id, cityDAO.getName());
        return cityRepository.findById(id).orElseThrow();
    }

    @Override
    public void delete(int cityId) {
        cityRepository.deleteById(cityId);
        logger.info("The city with the ID {} is deleted", cityId);
    }

    public boolean cityIdExists(int id) {
        return cityRepository.findById(id).isPresent();
    }

    public boolean cityNameExists(String name) {
        return cityRepository.findByName(name).isPresent();
    }
}

package com.ghilly.service;

import com.ghilly.model.entity.CityDAO;
import com.ghilly.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CityServiceRest implements CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceRest.class);
    private final CityRepository cityRepository;

    public CityServiceRest(CityRepository repository) {
        this.cityRepository = repository;
    }

    @Override
    public CityDAO create(CityDAO cityDAO) {
        cityRepository.save(cityDAO);
        logger.info("The city {} is created, the country is {}", cityDAO.getName(), cityDAO.getCountry().getName());
        return cityDAO;
    }

    @Override
    public CityDAO getCity(int cityId) {
        logger.info("The city with the ID {} is found.", cityId);
        return cityRepository.findById(cityId).get();
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
        return cityRepository.findById(id).get();
    }

    @Override
    public void delete(int cityId) {
        cityRepository.deleteById(cityId);
        logger.info("The city with ID {} is deleted", cityId);
    }
}

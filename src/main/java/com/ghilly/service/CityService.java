package com.ghilly.service;

import com.ghilly.exception.IdIsNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.City;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityService.class);
    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }

    public City create(String cityName, int countryId) {
//        if (!countryRepository.existsById(countryId))
//            throw new IdIsNotFoundException("The country with this ID " + countryId + " is not found!");
        cityName = cityName.replaceAll("[^a-zA-Z]", "");
        if (cityName.isEmpty()) throw new IllegalArgumentException("The city should have a name!");
        if (repository.findByName(cityName).isPresent())
            throw new NameAlreadyExistsException("The city with this name " + cityName + " already exists!");
        City city = repository.save(new City(cityName));
        logger.info("The city with this name {} is created.", cityName);
        return city;
    }
}

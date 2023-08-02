package com.ghilly.service;

import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.City;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkIdExists;
import static com.ghilly.utils.ValidationUtils.isWrongName;

public class CityServiceRest implements CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceRest.class);
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CityServiceRest(CityRepository repository, CountryRepository countryRepository) {
        this.cityRepository = repository;
        this.countryRepository = countryRepository;
    }

    @Override
    public City create(City city) {
        int countryId = city.getCountry().getId();
        String name = city.getName();
        checkIdExists(countryId, countryRepository, "The country with the ID " + countryId + " is not found.");
        checkNameIsWrong(city);
        if (cityRepository.findByName(name).isPresent())
            throw new NameAlreadyExistsException("The city with the name " + name + " already exists.");
        cityRepository.save(city);
        logger.info("The city with the name {} is created, country name is {}", name, city.getCountry().getName());
        return city;
    }

    @Override
    public City getCity(int cityId) {
        checkIdExists(cityId, cityRepository, "The city with the ID " + cityId + " is not found.");
        return cityRepository.findById(cityId).get();
    }

    @Override
    public List<City> getAllCities() {
        List<City> cities = (List<City>) cityRepository.findAll();
        logger.info("The list of cities is: {}", cities);
        return cities;
    }

    @Override
    public void update(City city) {
        int countryId = city.getCountry().getId();
        int cityId = city.getId();
        checkIdExists(countryId, countryRepository, "The country with the ID " + countryId + " is not found.");
        checkIdExists(cityId, cityRepository, "The city with the ID " + cityId + " is not found.");
        checkNameIsWrong(city);
        cityRepository.save(city);
        logger.info("The city with ID {} was updated, new name is {}.", cityId, city.getName());
    }

    @Override
    public void delete(int cityId) {
        checkIdExists(cityId, cityRepository, "The city with the ID " + cityId + " is not found.");
        cityRepository.deleteById(cityId);
        logger.info("The city with ID {} is deleted", cityId);
    }

    @Override
    public List<City> getAllCitiesByCountry(int countryId) {
        checkIdExists(countryId, countryRepository, "The country with the ID " + countryId + " is not found.");
        List<City> cities = new ArrayList<>();
        cityRepository.findAll().forEach(city -> {
            if (city.getCountry().getId() == countryId) {
                cities.add(city);
            }
        });
        logger.info("The city list for {} is received: {}", countryRepository.findById(countryId).get().getName(), cities);
        return cities;
    }

    private void checkNameIsWrong(City city) {
        String name = city.getName();
        if (isWrongName(name)) {
            throw new WrongNameException("Warning! \n The legal country name consists of letters that could be " +
                    "separated by one space or hyphen. \n The name is not allowed here: " + name);
        }
    }
}

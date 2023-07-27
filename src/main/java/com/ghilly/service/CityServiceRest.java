package com.ghilly.service;

import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.City;
import com.ghilly.model.Country;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public City create(String cityName, int countryId) {
        checkIdExists(countryId, countryRepository, "The country with the ID " + countryId + " is not found.");
        checkNameIsWrong(cityName);
        if (cityRepository.findByName(cityName).isPresent())
            throw new NameAlreadyExistsException("The city with the name " + cityName + " already exists.");
        Country country = countryRepository.findById(countryId).get();
        City city = cityRepository.save(new City(cityName, country));
        logger.info("The city with the name {} is created, country name is {}", cityName, country);
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
    public void update(int cityId, String newName) {
        checkIdExists(cityId, cityRepository, "The city with the ID " + cityId + " is not found.");
        checkNameIsWrong(newName);
        Country country = cityRepository.findById(cityId).get().getCountry();
        City city = new City(cityId, newName, country);
        cityRepository.save(city);
        logger.info("The city with ID {} was updated, new name is {}.", cityId, newName);
    }


    private void checkNameIsWrong(String countryName) {
        if (isWrongName(countryName)) {
            throw new WrongNameException("Warning! \n The legal country name consists of letters that could be " +
                    "separated by one space or hyphen. \n The name is not allowed here: " + countryName);
        }
    }
}

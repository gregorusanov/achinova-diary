package com.ghilly.service;

import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.City;
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
    public City create(City city) {
        checkIdExists(city.getCountry().getCountry_id(), countryRepository, "The country with the ID " +
                city.getCountry().getCountry_id() + " is not found.");
        checkNameIsWrong(city.getName());
        if (cityRepository.findByName(city.getName()).isPresent())
            throw new NameAlreadyExistsException("The city with the name " + city.getName() + " already exists.");
        cityRepository.save(city);
        logger.info("The city with the name {} is created, country name is {}", city.getName(),
                city.getCountry().getName());
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
        checkIdExists(city.getCountry().getCountry_id(), countryRepository, "The country with the ID " +
                city.getCountry().getCountry_id() + " is not found.");
        checkIdExists(city.getId(), cityRepository, "The city with the ID " +
                city.getId() + " is not found.");
        checkNameIsWrong(city.getName());
        cityRepository.save(city);
        logger.info("The city with ID {} was updated, new name is {}.", city.getId(), city.getName());
    }


    private void checkNameIsWrong(String countryName) {
        if (isWrongName(countryName)) {
            throw new WrongNameException("Warning! \n The legal country name consists of letters that could be " +
                    "separated by one space or hyphen. \n The name is not allowed here: " + countryName);
        }
    }
}

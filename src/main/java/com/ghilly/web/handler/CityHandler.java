package com.ghilly.web.handler;

import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.City;
import com.ghilly.model.Country;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import com.ghilly.service.CityServiceRest;
import com.ghilly.web.controller.CityController;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkIdExists;
import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CityHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityServiceRest service;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    String countryMsg = "The country ID ";
    String cityMsg = "The city ID ";

    public CityHandler(CityServiceRest service, CityRepository cityRepository, CountryRepository countryRepository) {
        this.service = service;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public CityDAO create(City city, int countryId) {
        checkIdExists(countryId, countryRepository, countryMsg);
        String name = city.getName();
        checkNameIsWrong(name);
        checkCityNameExists(name);
        logger.info("The user data are correct.");
        Country country = countryRepository.findById(countryId).get();
        return service.create(new CityDAO(name, country, city.isCapital()));
    }

    public CityDAO getCity(int cityId) {
        checkIdExists(cityId, cityRepository, cityMsg);
        logger.info("The user data are correct.");
        return service.getCity(cityId);
    }

    public List<CityDAO> getAllCities() {
        logger.info("Data processing.");
        return service.getAllCities();
    }

    public CityDAO update(City city, int cityId) {
        checkIdExists(cityId, cityRepository, cityMsg);
        String name = city.getName();
        checkNameIsWrong(name);
        checkCityNameExists(name);
        logger.info("The user data are correct.");
        Country country = cityRepository.findById(cityId).get().getCountry();
        CityDAO cityDAO = new CityDAO(cityId, name, country, city.isCapital());
        return service.update(cityDAO);
    }

    public void delete(int cityId) {
        checkIdExists(cityId, cityRepository, cityMsg);
        logger.info("The user data are correct.");
        service.delete(cityId);
    }

    private void checkCityNameExists(String name) {
        if (cityRepository.findByName(name).isPresent())
            throw new NameAlreadyExistsException("The city with the name " + name + " already exists.");
    }
}

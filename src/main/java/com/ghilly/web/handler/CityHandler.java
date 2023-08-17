package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.City;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import com.ghilly.web.controller.CityController;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CityHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityServiceRest cityServiceRest;
    private final CountryServiceRest countryServiceRest;

    public CityHandler(CityServiceRest cityServiceRest, CountryServiceRest countryServiceRest) {
        this.cityServiceRest = cityServiceRest;
        this.countryServiceRest = countryServiceRest;
    }

    public CityDAO create(City city, int countryId) {
        checkCountryIdExists(countryId);
        String name = city.getName();
        checkNameIsWrong(name);
        checkCityNameExists(name);
        logger.info("The user data are correct.");
        CountryDAO countryDAO = countryServiceRest.getCountryById(countryId);
        return cityServiceRest.create(new CityDAO(name, countryDAO, city.isCapital()));
    }

    public CityDAO getCity(int cityId) {
        checkCityIdExists(cityId);
        logger.info("The user data are correct.");
        return cityServiceRest.getCity(cityId);
    }

    public List<CityDAO> getAllCities() {
        logger.info("Data processing.");
        return cityServiceRest.getAllCities();
    }

    public CityDAO update(City city, int cityId) {
        checkCityIdExists(cityId);
        String name = city.getName();
        checkNameIsWrong(name);
        checkCityNameExists(name);
        logger.info("The user data are correct.");
        CountryDAO countryDAO = cityServiceRest.getCity(cityId).getCountry();
        CityDAO cityDAO = new CityDAO(cityId, name, countryDAO, city.isCapital());
        return cityServiceRest.update(cityDAO);
    }

    public void delete(int cityId) {
        checkCityIdExists(cityId);
        logger.info("The user data are correct.");
        cityServiceRest.delete(cityId);
    }



    private void checkCountryIdExists(int countryId) {
        if (!countryServiceRest.countryIdExists(countryId))
            throw new IdNotFoundException("The country ID " + countryId + " is not found.");
    }

    private void checkCityIdExists(int cityId) {
        if (!cityServiceRest.cityIdExists(cityId))
            throw new IdNotFoundException("The city ID " + cityId + " is not found.");
    }

    private void checkCityNameExists(String name) {
        if (cityServiceRest.cityNameExists(name))
            throw new NameAlreadyExistsException("The city name " + name + " already exists.");
    }
}

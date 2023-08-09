package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.Country;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import com.ghilly.web.controller.CityController;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CountryHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CountryServiceRest countryServiceRest;
    private final CityServiceRest cityServiceRest;

    public CountryHandler(CountryServiceRest countryServiceRest, CityServiceRest cityServiceRest) {
        this.countryServiceRest = countryServiceRest;
        this.cityServiceRest = cityServiceRest;
    }

    public CountryDAO create(Country country) {
        String name = country.getName();
        checkNameIsWrong(name);
        checkNameExists(name);
        logger.info("The user data are correct.");
        CountryDAO countryDAO = new CountryDAO(name);
        return countryServiceRest.create(countryDAO);
    }

    public List<CountryDAO> getAllCountries() {
        logger.info("Data processing.");
        return countryServiceRest.getAllCountries();
    }

    public CountryDAO getCountryById(int countryId) {
        checkIdExists(countryId);
        logger.info("The user data are correct.");
        return countryServiceRest.getCountryById(countryId);
    }

    public CountryDAO update(Country country, int countryId) {
        checkIdExists(countryId);
        String name = country.getName();
        checkNameIsWrong(name);
        checkNameExists(name);
        logger.info("The user data are correct.");
        return countryServiceRest.update(new CountryDAO(countryId, name));
    }

    public void delete(int countryId) {
        checkIdExists(countryId);
        logger.info("The user data are correct.");
        countryServiceRest.delete(countryId);
    }

    public CountryDAO getCountryByCityId(int cityId) {
        if (!cityServiceRest.cityIdExists(cityId))
            throw new IdNotFoundException("The city ID " + cityId + " is not found.");
        return cityServiceRest.getCity(cityId).getCountry();
    }

    private void checkIdExists(int id) {
        if (!countryServiceRest.countryIdExists(id)) {
            throw new IdNotFoundException("The country ID " + id + " is not found.");
        }
    }

    private void checkNameExists(String name) {
        if (countryServiceRest.countryNameExists(name)) {
            throw new NameAlreadyExistsException("The country name " + name + " already exists.");
        }
    }
}

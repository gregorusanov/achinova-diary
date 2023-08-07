package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.Country;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.service.CountryServiceRest;
import com.ghilly.web.controller.CityController;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CountryHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CountryServiceRest service;

    public CountryHandler(CountryServiceRest service) {
        this.service = service;
    }

    public CountryDAO create(Country country) {
        String name = country.getName();
        checkNameIsWrong(name);
        checkNameExists(name);
        logger.info("The user data are correct.");
        CountryDAO countryDAO = new CountryDAO(name);
        return service.create(countryDAO);
    }

    public List<CountryDAO> getAllCountries() {
        logger.info("Data processing.");
        return service.getAllCountries();
    }

    public CountryDAO getCountryById(int countryId) {
        checkIdExists(countryId);
        logger.info("The user data are correct.");
        return service.getCountryById(countryId);
    }

    public CountryDAO update(Country country, int countryId) {
        checkIdExists(countryId);
        String name = country.getName();
        checkNameIsWrong(name);
        checkNameExists(name);
        logger.info("The user data are correct.");
        return service.update(new CountryDAO(countryId, name));
    }

    public void delete(int countryId) {
        checkIdExists(countryId);
        logger.info("The user data are correct.");
        service.delete(countryId);
    }

    private void checkIdExists(int id) {
        if (!service.countryIdExists(id)) {
            throw new IdNotFoundException("The country ID " + id + " is not found.");
        }
    }

    private void checkNameExists(String name) {
        if (service.countryNameExists(name)) {
            throw new NameAlreadyExistsException("The country name " + name + " already exists.");
        }
    }
}

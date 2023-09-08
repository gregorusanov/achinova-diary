package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.service.CountryServiceRest;
import com.ghilly.web.controller.CityController;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CountryHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CountryServiceRest countryServiceRest;

    public CountryHandler(CountryServiceRest countryServiceRest) {
        this.countryServiceRest = countryServiceRest;
    }

    public CountryDAO create(CountryDAO country) {
        checkNameIsValid(country.getName());
        logger.info("The user data are correct.");
        return countryServiceRest.create(country);
    }

    public Set<CountryDAO> getAllCountries() {
        logger.info("Data processing.");
        return countryServiceRest.getAllCountries();
    }

    public CountryDAO getCountryById(int countryId) {
        checkIdExists(countryId);
        logger.info("The user data are correct.");
        return countryServiceRest.getCountryById(countryId);
    }

    public CountryDAO update(CountryDAO country) {
        checkIdExists(country.getId());
        checkNameIsValid(country.getName());
        logger.info("The user data are correct.");
        return countryServiceRest.update(country);
    }

    public void delete(int countryId) {
        checkIdExists(countryId);
        logger.info("The user data are correct.");
        countryServiceRest.delete(countryId);
    }

    public Set<CityDAO> getAllCitiesByCountryId(int countryId) {
        checkIdExists(countryId);
        return countryServiceRest.getAllCitiesByCountryId(countryId);
    }

    public CityDAO getCapitalByCountryId(int countryId) {
        checkIdExists(countryId);
        return countryServiceRest.getCapitalByCountryId(countryId);
    }

    private void checkNameIsValid(String name) {
        checkNameIsWrong(name);
        checkNameExists(name);
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

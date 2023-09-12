package com.ghilly.web.handler;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.service.CountryServiceRest;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CountryHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CountryHandler.class);
    private final CountryServiceRest countryServiceRest;

    public CountryHandler(CountryServiceRest countryServiceRest) {
        this.countryServiceRest = countryServiceRest;
    }

    public CountryEntity create(CountryEntity country) {
        checkNameIsValid(country.getName());
        logger.info("The user data are correct.");
        return countryServiceRest.create(country);
    }

    public Set<CountryEntity> getAllCountries() {
        logger.info("Data processing.");
        return countryServiceRest.getAllCountries();
    }

    public CountryEntity getCountryById(int countryId) {
        checkIdExists(countryId);
        logger.info("The user data are correct.");
        return countryServiceRest.getCountryById(countryId);
    }

    public CountryEntity update(CountryEntity country) {
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

    public Set<CityEntity> getAllCitiesByCountryId(int countryId) {
        checkIdExists(countryId);
        return countryServiceRest.getAllCitiesByCountryId(countryId);
    }

    public CityEntity getCapitalByCountryId(int countryId) {
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

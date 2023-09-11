package com.ghilly.web.handler;

import com.ghilly.exception.CapitalAlreadyExistsException;
import com.ghilly.exception.CityAlreadyExistsException;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.model.dao.CityDAO;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CityHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityHandler.class);
    private final CityServiceRest cityServiceRest;
    private final CountryServiceRest countryServiceRest;

    public CityHandler(CityServiceRest cityServiceRest, CountryServiceRest countryServiceRest) {
        this.cityServiceRest = cityServiceRest;
        this.countryServiceRest = countryServiceRest;
    }

    public CityDAO create(CityDAO city, int countryId) {
        checkCityNameAndCountryId(city.getName(), countryId);
        setCountryForCity(city, countryId);
        logger.info("The user data are correct.");
        return cityServiceRest.create(city);
    }

    public CityDAO getCity(int cityId) {
        checkCityIdExists(cityId);
        logger.info("The user data are correct.");
        return cityServiceRest.getCity(cityId);
    }

    public Set<CityDAO> getAllCities() {
        logger.info("Data processing.");
        return cityServiceRest.getAllCities();
    }

    public CityDAO update(CityDAO city, int countryId) {
        checkCityIdExists(city.getId());
        checkCityNameAndCountryId(city.getName(), countryId);
        setCountryForCity(city, countryId);
        logger.info("The user data are correct.");
        return cityServiceRest.update(city);
    }

    public void delete(int cityId) {
        checkCityIdExists(cityId);
        logger.info("The user data are correct.");
        cityServiceRest.delete(cityId);
    }

    private void checkCityNameAndCountryId(String name, int countryId) {
        checkNameIsWrong(name);
        checkCountryIdExists(countryId);
        checkCityNameExists(countryId, name);
    }

    private void setCountryForCity(CityDAO city, int countryId) {
        checkCapitalExists(countryId, city);
        city.setCountryDAO(countryServiceRest.getCountryById(countryId));
    }

    private void checkCityIdExists(int cityId) {
        if (!cityServiceRest.cityIdExists(cityId))
            throw new IdNotFoundException("The city ID " + cityId + " is not found.");
    }

    private void checkCountryIdExists(int countryId) {
        if (!countryServiceRest.countryIdExists(countryId))
            throw new IdNotFoundException("The country ID " + countryId + " is not found.");
    }

    private void checkCityNameExists(int countryId, String name) {
        if (cityServiceRest.theSameCityExists(countryId, name))
            throw new CityAlreadyExistsException("The city " + name + " already exists.");
    }

    private void checkCapitalExists(int countryId, CityDAO cityDAO) {
        if (cityDAO.isCapital() && countryServiceRest.getCapitalByCountryId(countryId) != null)
            throw new CapitalAlreadyExistsException("The capital for the country ID " + countryId +
                    " is already set. Try to update this city.");
    }
}

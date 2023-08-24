package com.ghilly.web.handler;

import com.ghilly.exception.CapitalAlreadyExistsException;
import com.ghilly.exception.CityAlreadyExistsException;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
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

    public CityDAO create(CityDAO city, int countryId) {
        checkBeforeSaving(city, countryId);
        logger.info("The user data are correct.");
        return cityServiceRest.create(city);
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

    public CityDAO update(CityDAO city, int countryId) {
        checkCityIdExists(city.getId());
        checkBeforeSaving(city, countryId);
        logger.info("The user data are correct.");
        return cityServiceRest.update(city);
    }

    public void delete(int cityId) {
        checkCityIdExists(cityId);
        logger.info("The user data are correct.");
        cityServiceRest.delete(cityId);
    }

    private void checkBeforeSaving(CityDAO city, int countryId) {
        checkCountryIdExists(countryId);
        city.setCountry(countryServiceRest.getCountryById(countryId));
        checkNameIsWrong(city.getName());
        checkCityExists(city);
        checkCapitalExists(city);
    }

    private void checkCityIdExists(int cityId) {
        if (!cityServiceRest.cityIdExists(cityId))
            throw new IdNotFoundException("The city ID " + cityId + " is not found.");
    }

    private void checkCountryIdExists(int countryId) {
        if (!countryServiceRest.countryIdExists(countryId))
            throw new IdNotFoundException("The country ID " + countryId + " is not found.");
    }

    private void checkCityExists(CityDAO cityDAO) {
        String name = cityDAO.getName();
        List<CityDAO> list = cityServiceRest.findAllCitiesWithTheSameName(name);
        list.forEach(cityDAO1 -> {
            if ((cityDAO1.getName().equals(cityDAO.getName())) & (cityDAO1.getCountry().equals(cityDAO.getCountry()))
            & (cityDAO1.isCapital() == cityDAO.isCapital())) {
                throw new CityAlreadyExistsException("The city " + name + " already exists.");
            }
        });
    }

    private void checkCapitalExists(CityDAO cityDAO) {
        CountryDAO countryDAO = cityDAO.getCountry();
        if (cityDAO.isCapital() && countryServiceRest.getCapitalByCountryId(countryDAO.getId()) != null)
            throw new CapitalAlreadyExistsException("The capital for the country ID " + countryDAO.getId() +
                    " is already set. Try to update this city.");
    }
}

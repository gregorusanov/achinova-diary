package com.ghilly.web.handler;

import com.ghilly.exception.CapitalAlreadyExistsException;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
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
        CountryDAO countryDAO = countryServiceRest.getCountryById(countryId);
        checkCountryIdExists(countryId);
        String name = city.getName();
        checkNameIsWrong(name);
        checkCityNameExists(name);
        setCapitalOrThrowCapitalAlreadyExistsEx(countryDAO, city);
        logger.info("The user data are correct.");
        CityDAO newCity = city.clone();
        newCity.setCountry(countryDAO);
        return cityServiceRest.create(newCity);
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

    public CityDAO update(CityDAO city) {
        int id = city.getId();
        checkCityIdExists(id);
        String name = city.getName();
        checkNameIsWrong(name);
        checkCityNameExists(name);
        logger.info("The user data are correct.");
        CityDAO cityDAO = city.clone();
        cityDAO.setCountry(cityServiceRest.getCity(id).getCountry());
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

    private void setCapitalOrThrowCapitalAlreadyExistsEx(CountryDAO countryDAO, CityDAO cityDAO) {
        if (cityDAO.isCapital() & countryServiceRest.getCapitalByCountryId(countryDAO.getId()) != null)
            throw new CapitalAlreadyExistsException("The capital for the country ID " + countryDAO.getId() +
                    " is already set. Try to update this city.");
    }
}

package com.ghilly.service;

import com.ghilly.model.dao.CityDAO;
import com.ghilly.model.dao.CountryDAO;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public CountryServiceRest(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public CountryDAO create(CountryDAO countryDAO) {
        CountryDAO toReturn = countryRepository.save(countryDAO);
        logger.info("The country {} is added by service. Info: {}", toReturn.getName(), toReturn);
        return toReturn;
    }

    @Override
    public Set<CountryDAO> getAllCountries() {
        Set<CountryDAO> countries = countryRepository.findAll();
        logger.info("The list of countries is: {}", countries);
        return countries;
    }

    @Override
    public CountryDAO getCountryById(int countryId) {
        logger.info("The country with the ID {} is found.", countryId);
        return countryRepository.findById(countryId).orElseThrow();
    }

    @Override
    public CountryDAO update(CountryDAO countryDAO) {
        CountryDAO toReturn = countryRepository.save(countryDAO);
        logger.info("The country with the ID {} is updated, new name is {}.", toReturn.getId(), toReturn.getName());
        return toReturn;
    }

    @Override
    public void delete(int countryId) {
        countryRepository.deleteById(countryId);
        logger.info("The country with the ID {} is deleted", countryId);
    }

    @Override
    public Set<CityDAO> getAllCitiesByCountryId(int countryId) {
        CountryDAO countryDAO = countryRepository.findById(countryId).orElseThrow();
        Set<CityDAO> citySet = countryDAO.getCitySet();
        logger.info("cityList = {}", citySet);
        return citySet;
    }

    @Override
    public CityDAO getCapitalByCountryId(int countryId) {
        return cityRepository.findCityDAOByCountryDAO_IdAndCapitalIsTrue(countryId).orElse(null);
    }

    public boolean countryIdExists(int id) {
        return countryRepository.findById(id).isPresent();
    }

    public boolean countryNameExists(String name) {
        return countryRepository.findByName(name).isPresent();
    }
}

package com.ghilly.service;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
    public List<CountryDAO> getAllCountries() {
        List<CountryDAO> countries = (List<CountryDAO>) countryRepository.findAll();
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
    public List<CityDAO> getAllCitiesByCountryId(int countryId) {
        CountryDAO countryDAO = countryRepository.findById(countryId).orElseThrow();
        List<CityDAO> cityList = countryDAO.getCityList();
        logger.info("cityList = {}", cityList);
        return cityList;
    }

    @Override
    public CityDAO getCapitalByCountryId(int countryId) {
        CountryDAO countryDAO = countryRepository.findById(countryId).orElseThrow();
        return cityRepository.findCityDAOByCountryDAOAndCapitalIsTrue(countryDAO).orElse(null);
    }

    public boolean countryIdExists(int id) {
        return countryRepository.findById(id).isPresent();
    }

    public boolean countryNameExists(String name) {
        return countryRepository.findByName(name).isPresent();
    }
}

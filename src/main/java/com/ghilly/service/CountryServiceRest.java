package com.ghilly.service;

import com.ghilly.model.entity.CountryDAO;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private final CountryRepository countryRepository;

    public CountryServiceRest(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
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
        return countryRepository.findById(countryId).get();
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

    public boolean countryIdExists(int id) {
        return countryRepository.findById(id).isPresent();
    }

    public boolean countryNameExists(String name) {
        return countryRepository.findByName(name).isPresent();
    }
}

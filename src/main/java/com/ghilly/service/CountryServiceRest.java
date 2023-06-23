package com.ghilly.service;

import com.ghilly.repository.CountryRepositoryRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private final CountryRepositoryRest repository;

    public CountryServiceRest(CountryRepositoryRest repository) {
        this.repository = repository;
    }


    @Override
    public void add(String countryName) {
        repository.insert(countryName);
        logger.info("The country {} was added by service.", countryName);
    }

    @Override
    public List<String> getAllCountries() {
        logger.info("The list of countries is:");
        return repository.takeAllCountries();
    }

    @Override
    public String getCountry(int countryId) {
        if (!repository.containsCountry(countryId)) {
            throw new IllegalArgumentException("The country is not found.");
        }
        return repository.takeCountry(countryId);
    }

    @Override
    public void upgrade(int countryId, String newName) {
        if (!repository.containsCountry(countryId)) {
            throw new IllegalArgumentException("The country is not found.");
        }
        String oldName = repository.takeCountry(countryId);
        repository.change(countryId, newName);
        logger.info("The country with ID {} was upgraded, old name is {}, new name is {}.", countryId, oldName, newName);
    }

    @Override
    public void remove(int countryId) {
        if (!repository.containsCountry(countryId)) {
            throw new IllegalArgumentException("The country is not found.");
        }
        repository.cut(countryId);
        logger.info("The country with ID {} was deleted", countryId);
    }
}

package com.ghilly.service;

import com.ghilly.repository.CountryRepositoryRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class CountryServiceRest implements Service {

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
    public List<String> receiveList() {
        repository.takeList();
        return new ArrayList<>();
    }

    @Override
    public String receiveCountry(int countryId) {
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
        repository.change(countryId, newName);
        logger.info("The country with ID {} was upgraded, new name is {}.", countryId, newName);
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

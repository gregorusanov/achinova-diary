package com.ghilly.service;

import com.ghilly.repository.CountryRepositoryRest;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class CountryServiceRest implements Service {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private CountryRepositoryRest repository;

    public CountryServiceRest(CountryRepositoryRest repository){
        this.repository = repository;
    }


    @Override
    public void add(String countryName) {
        logger.info("The country {} was added by service.", countryName);
    }

    @Override
    public List<List> receiveList() {
        return new ArrayList<>();
    }

    @Override
    public String receiveCountry(int countryId) {
        return "The country with this ID " + countryId + " is";
    }

    @Override
    public void upgrade(int countryId, String newName) {
        logger.info("The country with ID {} was upgraded, new name is {}.", countryId, newName);
    }

    @Override
    public void clear(int countryId) {
        logger.info("The country with ID {} was deleted", countryId);
    }
}

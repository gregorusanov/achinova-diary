package com.ghilly.service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);


    @Override
    public String add(String countryName) {
        logger.info("The country {} was added by service.", countryName);
        return countryName;
    }

    @Override
    public List<String> getAll() {
        logger.info("getAll");
        return new ArrayList<>();
    }

    @Override
    public String getCountry(int countryId) {
        logger.info("getCountry");
        return "There will be the country name";
    }

    @Override
    public String upgrade(int countryId, String newCountryName) {
        logger.info("The country with ID {} was upgraded, new name is {}.", countryId, newCountryName);
        return newCountryName;
    }

    @Override
    public String remove(int countryId) {
        logger.info("remove");
        return "There will be the country name";
    }
}

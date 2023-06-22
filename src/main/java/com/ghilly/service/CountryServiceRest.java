package com.ghilly.service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);


    @Override
    public String add(String countryName) {
        logger.info("The country {} was added by service.", countryName);
        return countryName;
    }

    @Override
    public List<List> receiveList() {
        logger.info("receiveList");
        return new ArrayList<>();
    }

    @Override
    public String receiveCountry(int countryId) {
        return "receive" + countryId;
    }

    @Override
    public String upgrade(int countryId, String newCountryName) {
        logger.info("The country with ID {} was upgraded, new name is {}.", countryId, newCountryName);
        return newCountryName;
    }

    @Override
    public String clear(int countryId) {
        return "clear" + countryId;
    }
}

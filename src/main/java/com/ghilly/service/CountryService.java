package com.ghilly.service;

//import com.ghilly.classes.Country;
//import com.ghilly.repository.CountryRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class CountryService implements Service {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

//    public CountryService (CountryRepository repository){
//        this.repository = repository;
//    }


    @Override
    public void add(String countryName) {
        logger.info("The country {} was added by service.", countryName);
    }

    @Override
    public List<String> receiveList() {
        return new ArrayList<>();
    }

    @Override
    public String receiveCountry(int countryId) {
        return "The country with this ID " + countryId + " is";
    }

    @Override
    public void upgrade(int id, String newName) {
        logger.info("The country with ID {} was upgraded, new name is {}.", id, newName);
    }

    @Override
    public void clear(int id) {
        logger.info("The country with ID {} was deleted", id);
    }
}

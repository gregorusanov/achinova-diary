package com.ghilly.service;

import com.ghilly.model.Countries;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private final CountryRepository repository;

    public CountryServiceRest(CountryRepository repository) {
        this.repository = repository;
    }


    @Override
    public Countries create(String countryName) {
        countryName = countryName.replaceAll("[^\\w+]", "");
        if (repository.findByName(countryName).isPresent()) {
            throw new IllegalArgumentException("Country with this name " + countryName + " already exists.");
        }
        Countries country = repository.save(new Countries(countryName));
        logger.info("The country {} was added by service.", countryName);
        return country;
    }

    @Override
    public List<Countries> getAllCountries() {
        ArrayList<Countries> countries = new ArrayList<>();
        repository.findAll().forEach(countries::add);
        logger.info("The list of countries is: {}", countries);
        return countries;
    }

    @Override
    public Countries getCountryById(int countryId) {
        if (!repository.findById(countryId).isPresent()) {
            throw new IllegalArgumentException("Countries with this ID " + countryId + " is not found.");
        }
        return repository.findById(countryId).get();
    }

    @Override
    public void update(Countries countries) {
        if (!repository.existsById(countries.getId())) {
            throw new IllegalArgumentException("The countries with the ID " + countries.getId() + " is not found.");
        }
        repository.save(new Countries(countries.getId(), countries.getName()));
        logger.info("The countries with ID {} was upgraded, new name is {}.", countries.getId(), countries.getName());
    }

    @Override
    public void delete(int countryId) {
        if (!repository.existsById(countryId)) {
            throw new IllegalArgumentException("The country with the ID " + countryId + " is not found.");
        }
        repository.deleteById(countryId);
        logger.info("The country with ID {} was deleted", countryId);
    }
}

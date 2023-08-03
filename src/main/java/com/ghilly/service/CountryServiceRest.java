package com.ghilly.service;

import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.Country;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkIdExists;
import static com.ghilly.utils.ValidationUtils.checkNameIsWrong;

public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private final CountryRepository repository;

    public CountryServiceRest(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Country create(Country country) {
        checkNameIsWrong(country.getName());
        String name = country.getName();
        if (repository.findByName(name).isPresent()) {
            throw new NameAlreadyExistsException("The country with this name " + name + " already exists.");
        }
        repository.save(country);
        logger.info("The country {} was added by service.", name);
        return country;
    }

    @Override
    public List<Country> getAllCountries() {
        ArrayList<Country> countries = new ArrayList<>();
        repository.findAll().forEach(countries::add);
        logger.info("The list of countries is: {}", countries);
        return countries;
    }

    @Override
    public Country getCountryById(int countryId) {
        checkIdExists(countryId, repository, "The country ID ");
        return repository.findById(countryId).get();
    }

    @Override
    public void update(Country country) {
        int id = country.getId();
        checkIdExists(id, repository, "The country ID ");
        checkNameIsWrong(country.getName());
        repository.save(country);
        logger.info("The country with ID {} was upgraded, new name is {}.", id, country.getName());
    }

    @Override
    public void delete(int countryId) {
        checkIdExists(countryId, repository, "The country ID ");
        repository.deleteById(countryId);
        logger.info("The country with ID {} was deleted", countryId);
    }
}

package com.ghilly.service;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.Country;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkNameIsCorrect;


public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private final CountryRepository repository;


    public CountryServiceRest(CountryRepository repository) {
        this.repository = repository;
    }


    @Override
    public Country create(String countryName) {
        checkNameIsCorrect(countryName);
        if (repository.findByName(countryName).isPresent()) {
            throw new NameAlreadyExistsException("The country with this name " + countryName + " already exists.");
        }
        Country country = repository.save(new Country(countryName));
        logger.info("The country {} was added by service.", countryName);
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
        if (!repository.findById(countryId).isPresent()) {
            throw new IdNotFoundException("The country with this ID " + countryId + " is not found.");
        }
        return repository.findById(countryId).get();
    }

    @Override
    public void update(Country country) {
        throwExceptionIfIdDoesNotExist(country.getId());
        checkNameIsCorrect(country.getName());
        repository.save(new Country(country.getId(), country.getName()));
        logger.info("The country with ID {} was upgraded, new name is {}.", country.getId(), country.getName());
    }

    @Override
    public void delete(int countryId) {
        throwExceptionIfIdDoesNotExist(countryId);
        repository.deleteById(countryId);
        logger.info("The country with ID {} was deleted", countryId);
    }

    public void throwExceptionIfIdDoesNotExist(int countryId) {
        if (!repository.existsById(countryId)) {
            throw new IdNotFoundException("The country with this ID " + countryId + " is not found.");
        }
    }
}

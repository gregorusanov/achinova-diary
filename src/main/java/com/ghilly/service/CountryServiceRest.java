package com.ghilly.service;

import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.exception.WrongNameException;
import com.ghilly.model.Country;
import com.ghilly.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ghilly.utils.ValidationUtils.checkIdExists;
import static com.ghilly.utils.ValidationUtils.isWrongName;

public class CountryServiceRest implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceRest.class);
    private final CountryRepository repository;

    public CountryServiceRest(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Country create(Country country) {
        checkNameIsWrong(country);
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
        checkIdExists(countryId, repository, "The country with the ID " + countryId + " is not found.");
        return repository.findById(countryId).get();
    }

    @Override
    public void update(Country country) {
        int id = country.getId();
        checkIdExists(id, repository, "The country with the ID " + id + " is not found.");
        checkNameIsWrong(country);
        repository.save(country);
        logger.info("The country with ID {} was upgraded, new name is {}.", id, country.getName());
    }

    @Override
    public void delete(int countryId) {
        checkIdExists(countryId, repository, "The country with the ID " + countryId + " is not found.");
        repository.deleteById(countryId);
        logger.info("The country with ID {} was deleted", countryId);
    }

    private void checkNameIsWrong(Country country) {
        String name = country.getName();
        if (isWrongName(name)) {
            throw new WrongNameException("Warning! \n The legal country name consists of letters that could be " +
                    "separated by one space or hyphen. \n The name is not allowed here: " + name);
        }
    }
}

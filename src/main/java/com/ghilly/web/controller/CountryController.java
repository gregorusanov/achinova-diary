package com.ghilly.web.controller;

import com.ghilly.model.dto.City;
import com.ghilly.model.dto.Country;
import com.ghilly.transformer.TransformerDAOandDTO;
import com.ghilly.web.handler.CountryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/countries")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
    private final CountryHandler countryHandler;

    public CountryController(CountryHandler countryHandler) {
        this.countryHandler = countryHandler;
    }

    @PostMapping("/")
    public ResponseEntity<Country> create(@RequestBody Country country) {
        country.setName(country.getName().toLowerCase());
        return Optional.of(country)
                .map(TransformerDAOandDTO::transformToCountryDAO)
                .map(countryHandler::create)
                .map(TransformerDAOandDTO::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/all")
    public ResponseEntity<Set<Country>> getAllCountries() {
        logger.info("Data processing.");
        Set<Country> allCountries = countryHandler.getAllCountries()
                .stream()
                .map(TransformerDAOandDTO::transformToCountryDTO)
                .sorted(Comparator.comparing(Country::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return ResponseEntity.ok().body(allCountries);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<Country> getCountryById(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        return Optional.of(countryHandler.getCountryById(countryId))
                .map(TransformerDAOandDTO::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<Country> update(@RequestBody Country country, @PathVariable int countryId) {
        country.setName(country.getName().toLowerCase());
        logger.info("The data are received from the user.");
        country.setId(countryId);
        return Optional.of(country)
                .map(TransformerDAOandDTO::transformToCountryDAO)
                .map(countryHandler::update)
                .map(TransformerDAOandDTO::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> deleteByCountryId(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        countryHandler.delete(countryId);
        return ResponseEntity.ok().body("The country with the ID " + countryId + " is deleted.");
    }

    @GetMapping("/{countryId}/cities/all")
    public ResponseEntity<Set<City>> getAllCitiesByCountryId(@PathVariable int countryId) {
        Set<City> allCitiesByCountry = countryHandler.getAllCitiesByCountryId(countryId)
                .stream()
                .map(TransformerDAOandDTO::transformToCityDTO)
                .sorted(Comparator.comparing(City::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return ResponseEntity.ok().body(allCitiesByCountry);
    }

    @GetMapping("/{countryId}/capital")
    public ResponseEntity<City> getCapitalByCountryId(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        return Optional.of(countryHandler.getCapitalByCountryId(countryId))
                .map(TransformerDAOandDTO::transformToCityDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
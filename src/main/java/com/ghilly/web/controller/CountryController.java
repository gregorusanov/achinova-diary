package com.ghilly.web.controller;


import com.ghilly.model.Country;
import com.ghilly.model.entity.CountryDAO;
import com.ghilly.web.handler.CountryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/countries")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
    private final CountryHandler countryHandler;

    public CountryController(CountryHandler countryHandler) {
        this.countryHandler = countryHandler;
    }


    @PostMapping("/")
    public ResponseEntity<CountryDAO> create(@RequestBody Country country) {
        logger.info("The data are received from the user.");
        CountryDAO countryDAO = countryHandler.create(country);
        return ResponseEntity.ok().body(countryDAO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDAO>> getAllCountries() {
        logger.info("Data processing.");
        List<CountryDAO> allCountries = countryHandler.getAllCountries();
        return ResponseEntity.ok().body(allCountries);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<CountryDAO> getCountry(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        CountryDAO countryDAO = countryHandler.getCountryById(countryId);
        return ResponseEntity.ok().body(countryDAO);
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<CountryDAO> update(@RequestBody Country country, @PathVariable int countryId) {
        logger.info("The data are received from the user.");
        CountryDAO countryDAO = countryHandler.update(country, countryId);
        return ResponseEntity.ok().body(countryDAO);
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> delete(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        countryHandler.delete(countryId);
        return ResponseEntity.ok().body("The country with the ID " + countryId + " is deleted.");
    }

    @GetMapping("/all/city/{cityId}")
    public ResponseEntity<CountryDAO> getCountryByCityId(@PathVariable int cityId) {
        logger.info("Getting the country by the city ID {} ", cityId);
        CountryDAO country = countryHandler.getCountryByCityId(cityId);
        return ResponseEntity.ok().body(country);
    }
}
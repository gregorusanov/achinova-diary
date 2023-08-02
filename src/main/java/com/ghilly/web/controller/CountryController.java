package com.ghilly.web.controller;


import com.ghilly.model.Country;
import com.ghilly.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/countries")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
    private final CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }


    @PostMapping("/")
    public ResponseEntity<Country> create(@RequestBody Country country) {
        service.create(country);
        logger.info("{} was created", country.getName());
        return ResponseEntity.ok().body(country);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Country>> getAllCountries() {
        logger.info("List of countries");
        List<Country> allCountries = service.getAllCountries();
        return ResponseEntity.ok().body(allCountries);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<Country> getCountry(@PathVariable int countryId) {
        logger.info("The country with this ID {} is: ", countryId);
        Country country = service.getCountryById(countryId);
        return ResponseEntity.ok().body(country);
    }

    @PutMapping("/")
    public ResponseEntity<Country> update(@RequestBody Country country) {
        service.update(country);
        logger.info("The country name for ID {} was changed to {}", country.getId(), country.getName());
        return ResponseEntity.ok().body(service.getCountryById(country.getId()));
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> delete(@PathVariable int countryId) {
        service.delete(countryId);
        logger.info("The country with ID {} was deleted.", countryId);
        return ResponseEntity.ok().body("The country with ID " + countryId + " was deleted.");
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<Country> getCountryByCityId(@PathVariable int cityId) {
        logger.info("Getting the country by the city ID {} ", cityId);
        Country country = service.getCountryByCityId(cityId);
        return ResponseEntity.ok().body(country);
    }
}
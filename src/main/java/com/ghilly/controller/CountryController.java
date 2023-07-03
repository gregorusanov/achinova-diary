package com.ghilly.controller;


import com.ghilly.model.Countries;
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
    public ResponseEntity<Countries> create(@RequestBody String countryName) {
        Countries country = service.create(countryName);
        logger.info("{} was created", countryName);
        return ResponseEntity.ok().body(country);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Countries>> getAllCountries() {
        logger.info("List of countries");
        List<Countries> allCountries = service.getAllCountries();
        return ResponseEntity.ok().body(allCountries);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<Countries> getCountry(@PathVariable int countryId) {
        logger.info("The country with this ID {} is: ", countryId);
        Countries country = service.getCountryById(countryId);
        return ResponseEntity.ok().body(country);
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<Countries> update(@PathVariable int countryId, @RequestBody String newName) {
        Countries countries = new Countries(countryId, newName);
        service.update(countries);
        logger.info("The countries name for ID {} was changed to {}", countries.getId(), countries.getName());
        return ResponseEntity.ok().body(service.getCountryById(countryId));
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> delete(@PathVariable int countryId) {
        service.delete(countryId);
        logger.info("The country with ID {} was deleted.", countryId);
        return ResponseEntity.ok().body("The country with ID " + countryId + " was deleted.");
    }
}
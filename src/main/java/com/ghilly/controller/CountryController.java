package com.ghilly.controller;


import com.ghilly.model.Country;
import com.ghilly.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void create(@RequestBody String countryName) {
        service.add(countryName);
        logger.info("{} was created", countryName);
    }

    @GetMapping("/")
    public List<Country> getCountries() {
        logger.info("List of countries");
        return service.getAllCountries();
    }

    @GetMapping("/{countryId}")
    public Country getCountry(@PathVariable int countryId) {
        logger.info("The country with this ID {} is: ", countryId);
        return service.getCountry(countryId);
    }

    @PutMapping("/{countryId}")
    public void update(@PathVariable int countryId, @RequestBody String newName) {
        Country country = new Country(countryId, newName);
        service.upgrade(country);
        logger.info("The country name for ID {} was changed to {}", country.getId(), country.getName());
    }

    @DeleteMapping("/{countryId}")
    public void delete(@PathVariable int countryId) {
        service.remove(countryId);
        logger.info("The country with ID {} was deleted.", countryId);
    }
}
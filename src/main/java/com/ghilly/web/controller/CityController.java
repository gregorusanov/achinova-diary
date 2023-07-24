package com.ghilly.web.controller;

import com.ghilly.model.City;
import com.ghilly.service.CityServiceRest;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/countries/{countryId}/cities")
public class CityController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityServiceRest service;

    public CityController(CityServiceRest service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<City> create(@RequestBody String cityName, @PathVariable int countryId) {
        City city = service.create(cityName, countryId);
        logger.info("The city with the name {} is created.", cityName);
        return ResponseEntity.ok().body(city);
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<City> getCity(@PathVariable int cityId, @PathVariable int countryId) {
        City city = service.getCity(cityId, countryId);
        logger.info("The city with the ID {} is: {}", cityId, city.getName());
        return ResponseEntity.ok().body(city);
    }
}

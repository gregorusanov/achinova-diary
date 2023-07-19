package com.ghilly.web.controller;

import com.ghilly.model.City;
import com.ghilly.service.CityService;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/countries/{countryId}/cities")
public class CityController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityService service;

    public CityController(CityService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<City> create (@RequestBody String cityName, @PathVariable int countryId) {
        City city = service.create(cityName, countryId);
        logger.info("The city with this name {} is created.", cityName);
        return ResponseEntity.ok().body(city);
    }
}

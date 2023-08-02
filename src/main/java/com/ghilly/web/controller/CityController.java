package com.ghilly.web.controller;

import com.ghilly.model.City;
import com.ghilly.service.CityServiceRest;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RequestMapping("/countries/cities")
public class CityController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityServiceRest service;

    public CityController(CityServiceRest service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<City> create(@RequestBody City city) {
        service.create(city);
        logger.info("The city with the name {} is created.", city.getName());
        return ResponseEntity.ok().body(city);
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<City> getCity(@PathVariable int cityId) {
        City city = service.getCity(cityId);
        logger.info("The city with the ID {} is: {}", cityId, city.getName());
        return ResponseEntity.ok().body(city);
    }

    @GetMapping("/all")
    public ResponseEntity<List<City>> getAllCities() {
        logger.info("You are getting the list of cities.");
        List<City> allCities = service.getAllCities();
        return ResponseEntity.ok().body(allCities);
    }

    @PutMapping("/")
    public ResponseEntity<City> update(@RequestBody City city) {
        service.update(city);
        logger.info("The city name for city ID {} is changing to {}", city.getId(), city.getName());
        return ResponseEntity.ok().body(service.getCity(city.getId()));
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<String> deleteCity(@PathVariable int cityId) {
        service.delete(cityId);
        return ResponseEntity.ok().body("The city with the ID " + cityId + " is deleted");
    }
}
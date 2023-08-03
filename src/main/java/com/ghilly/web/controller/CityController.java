package com.ghilly.web.controller;

import com.ghilly.model.City;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.web.handler.CityHandler;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/cities")
public class CityController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityHandler cityHandler;

    public CityController(CityHandler cityHandler) {
        this.cityHandler = cityHandler;
    }

    @PostMapping("/country_id/{countryId}")
    public ResponseEntity<CityDAO> create(@RequestBody City city, @PathVariable int countryId) {
        logger.info("The data are received from the user.");
        CityDAO cityDAO = cityHandler.create(city, countryId);
        return ResponseEntity.ok().body(cityDAO);
    }

    @GetMapping("/city_id/{cityId}")
    public ResponseEntity<CityDAO> getCity(@PathVariable int cityId) {
        logger.info("Data processing.");
        CityDAO cityDAO = cityHandler.getCity(cityId);
        return ResponseEntity.ok().body(cityDAO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CityDAO>> getAllCities() {
        logger.info("Data processing.");
        List<CityDAO> allCities = cityHandler.getAllCities();
        return ResponseEntity.ok().body(allCities);
    }

    @PutMapping("/city_id/{cityId}")
    public ResponseEntity<CityDAO> update(@RequestBody City city, @PathVariable int cityId) {
        logger.info("The data are received from the user.");
        CityDAO cityDAO = cityHandler.update(city, cityId);
        return ResponseEntity.ok().body(cityDAO);
    }

    @DeleteMapping("/city_id/{cityId}")
    public ResponseEntity<String> deleteCity(@PathVariable int cityId) {
        cityHandler.delete(cityId);
        return ResponseEntity.ok().body("The city with the ID " + cityId + " is deleted");
    }
}
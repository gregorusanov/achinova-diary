package com.ghilly.web.controller;

import com.ghilly.model.dto.City;
import com.ghilly.transformer.EntityTransformer;
import com.ghilly.web.handler.CityHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RequestMapping("/cities")
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityHandler cityHandler;

    public CityController(CityHandler cityHandler) {
        this.cityHandler = cityHandler;
    }

    @PostMapping("/")
    public ResponseEntity<City> create(@RequestBody City city) {
        logger.info("The data are received from the user. City: [{}]", city);
        city.setName(city.getName().toLowerCase());
        return Optional.of(city)
                .map(cityToTransform -> EntityTransformer.transformToCityEntity(city))
                .map(cityEntity -> cityHandler.create(cityEntity, city.getCountryId()))
                .map(EntityTransformer::transformToCity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<City> getCity(@PathVariable int cityId) {
        logger.info("The data are received from the user.");
        return Optional.of(cityHandler.getCity(cityId))
                .map(EntityTransformer::transformToCity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<Set<City>> getAllCities() {
        logger.info("Data processing.");
        Set<City> cities = cityHandler.getAllCities().stream()
                .map(EntityTransformer::transformToCity)
                .sorted(Comparator.comparing(City::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return ResponseEntity.ok(cities);
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<City> update(@RequestBody City city, @PathVariable int cityId) {
        logger.info("The data are received from the user.");
        city.setName(city.getName().toLowerCase());
        city.setId(cityId);
        return Optional.of(city)
                .map(cityToTransform -> EntityTransformer.transformToCityEntity(city))
                .map(cityEntity -> cityHandler.update(cityEntity, city.getCountryId()))
                .map(EntityTransformer::transformToCity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<String> deleteCity(@PathVariable int cityId) {
        logger.info("The data are received from the user.");
        cityHandler.delete(cityId);
        return ResponseEntity.ok().body("The city with the ID " + cityId + " is deleted.");
    }
}
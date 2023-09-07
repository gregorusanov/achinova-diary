package com.ghilly.web.controller;

import com.ghilly.model.DTO.CityDTO;
import com.ghilly.transformer.TransformerDAOandDTO;
import com.ghilly.web.handler.CityHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequestMapping("/cities")
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityHandler cityHandler;

    public CityController(CityHandler cityHandler) {
        this.cityHandler = cityHandler;
    }

    @PostMapping("/")
    public ResponseEntity<CityDTO> create(@RequestBody CityDTO city) {
        logger.info("The data are received from the user. City: [{}]", city);
        city.setName(city.getName().toLowerCase());
        return Optional.of(city)
                .map(cityDTO -> TransformerDAOandDTO.transformToCityDAO(city))
                .map(cityDAO -> cityHandler.create(cityDAO, city.getCountryId()))
                .map(TransformerDAOandDTO::transformToCityDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityDTO> getCity(@PathVariable int cityId) {
        logger.info("The data are received from the user.");
        return Optional.of(cityHandler.getCity(cityId))
                .map(TransformerDAOandDTO::transformToCityDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CityDTO>> getAllCities() {
        logger.info("Data processing.");
        List<CityDTO> cityDTOS = cityHandler.getAllCities().stream()
                .map(TransformerDAOandDTO::transformToCityDTO)
                .toList();
        return ResponseEntity.ok(cityDTOS);
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<CityDTO> update(@RequestBody CityDTO city, @PathVariable int cityId) {
        logger.info("The data are received from the user.");
        city.setName(city.getName().toLowerCase());
        city.setId(cityId);
        return Optional.of(city)
                .map(cityDTO -> TransformerDAOandDTO.transformToCityDAO(city))
                .map(cityDAO -> cityHandler.update(cityDAO, city.getCountryId()))
                .map(TransformerDAOandDTO::transformToCityDTO)
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
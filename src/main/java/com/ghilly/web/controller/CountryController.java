package com.ghilly.web.controller;

import com.ghilly.model.DTO.CityDTO;
import com.ghilly.model.DTO.CountryDTO;
import com.ghilly.transformer.TransformerFromDAOtoDTOAndBack;
import com.ghilly.web.handler.CountryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequestMapping("/countries")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
    private final CountryHandler countryHandler;

    public CountryController(CountryHandler countryHandler) {
        this.countryHandler = countryHandler;
    }

    @PostMapping("/")
    public ResponseEntity<CountryDTO> create(@RequestBody CountryDTO country) {
        return Optional.of(country)
                .map(TransformerFromDAOtoDTOAndBack::transformToCountryDAO)
                .map(countryHandler::create)
                .map(TransformerFromDAOtoDTOAndBack::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        logger.info("Data processing.");
        List<CountryDTO> allCountries = countryHandler.getAllCountries()
                .stream()
                .map(TransformerFromDAOtoDTOAndBack::transformToCountryDTO)
                .sorted(Comparator.comparing(CountryDTO::getName))
                .toList();
        return ResponseEntity.ok().body(allCountries);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        return Optional.of(countryHandler.getCountryById(countryId))
                .map(TransformerFromDAOtoDTOAndBack::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<CountryDTO> update(@RequestBody CountryDTO country, @PathVariable int countryId) {
        logger.info("The data are received from the user.");
        country.setId(countryId);
        return Optional.of(country)
                .map(TransformerFromDAOtoDTOAndBack::transformToCountryDAO)
                .map(countryHandler::update)
                .map(TransformerFromDAOtoDTOAndBack::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> deleteByCountryId(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        countryHandler.delete(countryId);
        return ResponseEntity.ok().body("The country with the ID " + countryId + " is deleted.");
    }

    @GetMapping("/{countryId}/cities/all")
    public ResponseEntity<List<CityDTO>> getAllCitiesByCountryId(@PathVariable int countryId) {
        List<CityDTO> allCitiesByCountry = countryHandler.getAllCitiesByCountryId(countryId)
                .stream()
                .map(TransformerFromDAOtoDTOAndBack::transformToCityDTO)
                .sorted(Comparator.comparing(CityDTO::getName))
                .toList();
        return ResponseEntity.ok().body(allCitiesByCountry);
    }

    @GetMapping("/{countryId}/capital")
    public ResponseEntity<CityDTO> getCapitalByCountryId(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        return Optional.of(countryHandler.getCapitalByCountryId(countryId))
                .map(TransformerFromDAOtoDTOAndBack::transformToCityDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
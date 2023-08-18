package com.ghilly.web.controller;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.model.DTO.CityDTO;
import com.ghilly.model.DTO.CountryDTO;
import com.ghilly.transformer.TransformerDAODTO;
import com.ghilly.web.validator.CountryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/countries")
//TODO add Optional.of to all methods
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);
    private final CountryHandler countryHandler;

    public CountryController(CountryHandler countryHandler) {
        this.countryHandler = countryHandler;
    }

    @PostMapping("/")
    public ResponseEntity<CountryDTO> create(@RequestBody CountryDTO country) {
        return Optional.of(country)
                .map(TransformerDAODTO::transformToCountryDAO)
                .map(countryHandler::create)
                .map(TransformerDAODTO::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        logger.info("Data processing.");
        List<CountryDTO> allCountries = countryHandler.getAllCountries().
                stream().map(TransformerDAODTO::transformToCountryDTO).toList();
        return ResponseEntity.ok().body(allCountries);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        return Optional.of(countryHandler.getCountryById(countryId))
                .map(TransformerDAODTO::transformToCountryDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<CountryDTO> update(@RequestBody CountryDTO country, @PathVariable int countryId) {
        logger.info("The data are received from the user.");
        country.setId(countryId);
        CountryDAO countryDAO = TransformerDAODTO.transformToCountryDAO(country);
        CountryDTO updatedCountry = TransformerDAODTO.transformToCountryDTO(countryHandler.update(countryDAO));
        return ResponseEntity.ok().body(updatedCountry);


    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<String> deleteByCountryId(@PathVariable int countryId) {
        logger.info("The data are received from the user.");
        countryHandler.delete(countryId);
        return ResponseEntity.ok().body("The country with the ID " + countryId + " is deleted.");
    }

    @GetMapping("/{countryId}/cities/all")
    public ResponseEntity<List<CityDTO>> getAllCitiesByCountryId(@PathVariable int countryId) {
        List<CityDTO> allCitiesByCountry = countryHandler.getAllCitiesByCountryId(countryId).stream()
                .map(TransformerDAODTO::transformToCityDTO)
                .toList();
        return ResponseEntity.ok().body(allCitiesByCountry);
    }


}
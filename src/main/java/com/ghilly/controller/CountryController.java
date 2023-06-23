package com.ghilly.controller;


import com.ghilly.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService service;
    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    public CountryController(CountryService service) {
        this.service = service;
    }


    @PostMapping("/")
    public void create(@RequestBody String countryName) {
        service.add(countryName);
        logger.info("{} was created.", countryName);
    }

    @GetMapping("/")
    public List<String> getCountries() {
        logger.info("List of countries");
        return service.getAll();
    }

    @GetMapping("/{countryId}")
    public String getCountry(@PathVariable int countryId) {
        logger.info("The country with this ID {} is: ", countryId);
        return service.getCountry(countryId);
    }

    @PutMapping("/{countryId}")
    public void update(@PathVariable int countryId, @RequestBody String newName) {
        String oldName = service.getCountry(countryId);
        service.upgrade(countryId, newName);
        logger.info("The country name for ID {} was changed from {} to {}", countryId, oldName, newName);
    }

    @DeleteMapping("/{countryId}")
    public void delete(@PathVariable int countryId) {
        service.remove(countryId);
        logger.info("The country with ID {} was deleted.", countryId);
    }
}

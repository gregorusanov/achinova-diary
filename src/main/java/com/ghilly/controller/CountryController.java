package com.ghilly.controller;


import com.ghilly.service.CountryServiceRest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    CountryServiceRest service;

    public CountryController(CountryServiceRest service){
        this.service = service;
    }


    @PostMapping("/")
    public void create(@RequestBody String countryName) {
        service.add(countryName);
    }

    @GetMapping("/")
    public List<String> getCountries() {
        service.receiveList();
        return new ArrayList<>();
    }

    @GetMapping("/{countryId}")
    public void getCountry(@PathVariable int countryId) {
        service.receiveCountry(countryId);
    }

    @PutMapping("/{countryId}")
    public void update(@PathVariable int countryId, @RequestBody String newName) {
        service.upgrade(countryId, newName);
    }

    @DeleteMapping("/{countryId}")
    public void delete(@PathVariable int countryId) {
        service.clear(countryId);
    }
}

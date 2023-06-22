package com.ghilly.controller;


import com.ghilly.service.CountryServiceRest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {


    private final CountryServiceRest countryServiceRest;

    public CountryController(CountryServiceRest countryServiceRest) {
        this.countryServiceRest = countryServiceRest;
    }

    @PostMapping("/")
    public void create(@RequestBody String countryName) {
        System.out.println(countryName);
    }

    @GetMapping("/")
    public List<String> getCountries() {
        return new ArrayList<>();
    }

    @GetMapping("/{countryId}")
    public String getCountry(@PathVariable int countryId) {
        return "get" + countryId;
    }

    @PutMapping("/{countryId}")
    public void update(@PathVariable int countryId, @RequestBody String newName) {
        System.out.println("update" + countryId + "newName " + newName);
    }

    @DeleteMapping("/{countryId}")
    public void delete(@PathVariable int countryId) {
        System.out.println("delete" + countryId);
    }
}

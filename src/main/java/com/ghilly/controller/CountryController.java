package com.ghilly.controller;


import com.ghilly.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {


    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/*")
    public void create(@RequestBody String name) {
        System.out.println(name);
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

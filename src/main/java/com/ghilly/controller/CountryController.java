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

    @PostMapping("/")
    public void create(@RequestBody String name) {
        countryService.add(name);
        System.out.println(name);
    }

    @GetMapping("/")
    public List<String> getCountries() {
        return countryService.receiveList();
    }

    @GetMapping("/{countryId}")
    public String getCountry(@PathVariable int countryId) {
        return countryService.receiveCountry(countryId);
    }

    @PutMapping("/{countryId}")
    public void update(@PathVariable int countryId, @RequestBody String newName) {
        countryService.upgrade(countryId, newName);
        System.out.println("update" + countryId + "newName " + newName);
    }

    @DeleteMapping("/{countryId}")
    public void delete(@PathVariable int countryId) {
        countryService.clear(countryId);
        System.out.println("delete" + countryId);
    }
}

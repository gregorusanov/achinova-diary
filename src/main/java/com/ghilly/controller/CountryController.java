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
        System.out.println("create" + countryName);
    }

    @GetMapping("/")
    public List<String> getCountries() {
        service.receiveList();
        System.out.println("get countries");
        return new ArrayList<>();
    }

    @GetMapping("/{countryId}")
    public void getCountry(@PathVariable int countryId) {
        service.receiveCountry(countryId);
    }

    @PutMapping("/{countryId}")
    public void update(@PathVariable int countryId, @RequestBody String newName) {
        service.upgrade(countryId, newName);
        System.out.println("update " + countryId + " new name is " + newName);
    }

    @DeleteMapping("/{countryId}")
    public void delete(@PathVariable int countryId) {
        service.remove(countryId);
        System.out.println("delete " + countryId);
    }
}

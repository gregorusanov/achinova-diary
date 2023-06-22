package com.ghilly.controller;


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {


    @PostMapping("/")
    public String create(@RequestBody String name) {
        return name;
    }

    @GetMapping("/")
    public List<String> getCountries() {
        return new ArrayList<>();
    }

    @GetMapping("/{countryId}")
    public String getCountry(@PathVariable int countryId) {
        return "get country" + countryId;
    }

    @PutMapping("/{countryId}")
    public String update(@PathVariable int countryId, @RequestBody String newName) {
        return "update" + countryId + "newName " + newName;
    }

    @DeleteMapping("/{countryId}")
    public String delete(@PathVariable int countryId) {
        return "delete" + countryId;
    }
}

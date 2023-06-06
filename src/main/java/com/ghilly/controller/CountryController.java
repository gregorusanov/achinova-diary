package com.ghilly.controller;



import com.ghilly.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/country")
public class CountryController {


    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello from Country and service " + countryService.serviceSaysHello();
    }

    @GetMapping("/bye")
    public String sayBye(){
        return "Bye from Country and service " + countryService.serviceSaysHello();
    }
}

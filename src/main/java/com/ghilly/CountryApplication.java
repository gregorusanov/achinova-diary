package com.ghilly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class CountryApplication {
    @RequestMapping("/")
    String home(){
        return "Welcome to the Travel Diary";
    }

    public static void main(String[] args) {
        SpringApplication.run(CountryApplication.class, args);
    }

}

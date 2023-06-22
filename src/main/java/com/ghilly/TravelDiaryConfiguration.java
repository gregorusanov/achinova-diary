package com.ghilly.configuration;

import com.ghilly.service.CountryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelDiaryConfiguration {

    @Bean
    public CountryService countryService() {
        return new CountryService();
    }
}
package com.ghilly.configuration;

import com.ghilly.service.CountryServiceRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelDiaryConfiguration {

    @Bean
    public CountryServiceRest countryService() {
        return new CountryServiceRest();
    }
}
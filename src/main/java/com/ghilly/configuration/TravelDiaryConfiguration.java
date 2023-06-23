package com.ghilly.configuration;

import com.ghilly.repository.CountryRepositoryRest;
import com.ghilly.service.CountryServiceRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class TravelDiaryConfiguration {

    @Bean
    public CountryRepositoryRest repositoryRest() {
        return new CountryRepositoryRest();
    }
    @Bean
    public CountryServiceRest countryService() {
        return new CountryServiceRest(repositoryRest());
    }
}

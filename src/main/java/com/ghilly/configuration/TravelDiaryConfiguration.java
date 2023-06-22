package com.ghilly.configuration;

import com.ghilly.repository.CountryRepositoryRest;
import com.ghilly.service.CountryServiceRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelDiaryConfiguration {

    @Bean
    public CountryServiceRest countryService() {
        return new CountryServiceRest(new CountryRepositoryRest(countryRepository));
    }

    @Bean
    public CountryRepositoryRest repositoryRest() {
        return new CountryRepositoryRest(countryRepository);
    }
}

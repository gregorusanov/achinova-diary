package com.ghilly.configuration;

import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import com.ghilly.web.controller.CityController;
import com.ghilly.web.controller.CountryController;
import com.ghilly.web.handler.CityHandler;
import com.ghilly.web.handler.CountryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelDiaryConfiguration {
    @Bean
    public CountryServiceRest countryService(CountryRepository repository) {
        return new CountryServiceRest(repository);
    }

    @Bean
    public CountryHandler countryHandler(CountryServiceRest countryServiceRest) {
        return new CountryHandler(countryServiceRest);
    }

    @Bean
    public CountryController countryController(CountryHandler countryHandler) {
        return new CountryController(countryHandler);
    }

    @Bean
    public CityServiceRest cityService(CityRepository cityRepository) {
        return new CityServiceRest(cityRepository);
    }

    @Bean
    public CityHandler cityHandler(CityServiceRest cityServiceRest, CountryServiceRest countryServiceRest) {
        return new CityHandler(cityServiceRest, countryServiceRest);
    }

    @Bean
    public CityController cityController(CityHandler cityHandler) {
        return new CityController(cityHandler);
    }
}

package com.ghilly.configuration;

import com.ghilly.repository.CityRepository;
import com.ghilly.service.CityServiceRest;
import com.ghilly.web.controller.CityController;
import com.ghilly.web.controller.CountryController;
import com.ghilly.repository.CountryRepository;
import com.ghilly.service.CountryService;
import com.ghilly.service.CountryServiceRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelDiaryConfiguration {
    @Bean
    public CountryServiceRest countryService(CountryRepository countryRepository, CityRepository cityRepository) {
        return new CountryServiceRest(countryRepository, cityRepository);
    }

    @Bean
    public CountryController countryController(CountryService service) {
        return new CountryController(service);
    }

    @Bean
    public CityServiceRest cityService(CityRepository cityRepository, CountryRepository countryRepository) {
        return new CityServiceRest(cityRepository, countryRepository);
    }

    @Bean
    public CityController cityController(CityServiceRest service) {
        return new CityController(service);
    }
}

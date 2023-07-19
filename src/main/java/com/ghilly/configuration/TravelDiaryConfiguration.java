package com.ghilly.configuration;

import com.ghilly.repository.CityRepository;
import com.ghilly.service.CityService;
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
    public CountryServiceRest countryService(CountryRepository repository) {
        return new CountryServiceRest(repository);
    }

    @Bean
    public CountryController countryController(CountryService service) {
        return new CountryController(service);
    }

    @Bean
    public CityService cityService(CityRepository cityRepository) {
        return new CityService(cityRepository);
    }

    @Bean
    public CityController cityController(CityService service) {
        return new CityController(service);
    }
}

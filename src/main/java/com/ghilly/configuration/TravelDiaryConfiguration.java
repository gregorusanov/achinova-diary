package com.ghilly.configuration;

import com.ghilly.repository.CityRepository;
import com.ghilly.service.CityServiceRest;
import com.ghilly.web.controller.CityController;
import com.ghilly.web.controller.CountryController;
import com.ghilly.repository.CountryRepository;
import com.ghilly.service.CountryService;
import com.ghilly.service.CountryServiceRest;
import com.ghilly.web.handler.CityHandler;
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
    public CityServiceRest cityService(CityRepository cityRepository) {
        return new CityServiceRest(cityRepository);
    }

    @Bean
    public CityHandler cityHandler(CityServiceRest cityServiceRest, CityRepository cityRepository, CountryRepository
            countryRepository) {
        return new CityHandler(cityServiceRest, cityRepository, countryRepository);
    }

    @Bean
    public CityController cityController(CityHandler cityHandler) {
        return new CityController(cityHandler);
    }
}

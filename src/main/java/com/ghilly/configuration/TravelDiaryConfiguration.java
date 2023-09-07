package com.ghilly.configuration;

import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import com.ghilly.repository.TravelDiaryRepository;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.CountryServiceRest;
import com.ghilly.service.TravelDiaryServiceRest;
import com.ghilly.web.controller.CityController;
import com.ghilly.web.controller.CountryController;
import com.ghilly.web.controller.TravelDiaryController;
import com.ghilly.web.handler.CityHandler;
import com.ghilly.web.handler.CountryHandler;
import com.ghilly.web.handler.TravelDiaryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelDiaryConfiguration {
    @Bean
    public CountryServiceRest countryService(CountryRepository countryRepository, CityRepository cityRepository) {
        return new CountryServiceRest(countryRepository, cityRepository);
    }

    @Bean
    public CountryHandler countryValidator(CountryServiceRest countryServiceRest) {
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

    @Bean
    public TravelDiaryServiceRest travelDiaryServiceRest(CityRepository cityRepository,
                                                         TravelDiaryRepository travelDiaryRepository) {
        return new TravelDiaryServiceRest(cityRepository, travelDiaryRepository);
    }

    @Bean
    public TravelDiaryHandler travelDiaryHandler(CityServiceRest serviceRest, TravelDiaryServiceRest travelDiaryServiceRest) {
        return new TravelDiaryHandler(serviceRest, travelDiaryServiceRest);
    }

    @Bean
    public TravelDiaryController travelDiaryController(TravelDiaryHandler handler) {
        return new TravelDiaryController(handler);
    }
}

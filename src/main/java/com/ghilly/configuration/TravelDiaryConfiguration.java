package com.ghilly.configuration;

import com.ghilly.controller.CountryController;
import com.ghilly.repository.CountryRepository;
import com.ghilly.repository.CountryRepositoryRest;
import com.ghilly.service.CountryService;
import com.ghilly.service.CountryServiceRest;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TravelDiaryConfiguration {

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }
    @Bean
    public CountryRepositoryRest repositoryRest() {
        return new CountryRepositoryRest();
    }

    @Bean
    public CountryServiceRest countryService(CountryRepository repository) {
        return new CountryServiceRest(repository);
    }

    @Bean
    public CountryController countryController(CountryService service) {
        return new CountryController(service);
    }
}

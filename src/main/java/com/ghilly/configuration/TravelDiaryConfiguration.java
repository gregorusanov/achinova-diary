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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.ghilly.repository")
public class TravelDiaryConfiguration {

    private final Environment env;

    public TravelDiaryConfiguration(Environment env) {
        this.env = env;
    }

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


    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setSchema(env.getProperty("spring.datasource.hikari.schema"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.ghilly.model.dao");
        factory.setDataSource(dataSource());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}

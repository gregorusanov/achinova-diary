package com.ghilly.repository;

import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<CityDAO, Integer> {
    Optional<CityDAO> findByName(String cityName);

    Optional<CityDAO> findCityDAOByCountryDAOAndCapitalIsTrue(CountryDAO countryDAO);
    Optional<List<CityDAO>> findAllByCountryDAO(CountryDAO countryDAO);
}
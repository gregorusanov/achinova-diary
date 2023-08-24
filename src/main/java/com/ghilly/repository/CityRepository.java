package com.ghilly.repository;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<CityDAO, Integer> {
    Optional<CityDAO> findByName(String cityName);

    Optional<CityDAO> findCityDAOByCountryDAOIdAndCapitalIsTrue(int countryId);
    Optional<List<CityDAO>> findAllByName(String name);
}

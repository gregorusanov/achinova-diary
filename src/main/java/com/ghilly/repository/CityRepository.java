package com.ghilly.repository;

import com.ghilly.model.DAO.CityDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<CityDAO, Integer> {
    Optional<CityDAO> findByName(String cityName);

    Optional<CityDAO> findCityDAOByCountryDAO_IdAndCapitalIsTrue(int countryId);

    boolean existsByCountryDAO_IdAndName(int countryId, String name);
}

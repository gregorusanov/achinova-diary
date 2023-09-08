package com.ghilly.repository;

import com.ghilly.model.DAO.CityDAO;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CityRepository extends CrudRepository<CityDAO, Integer> {

    @Override
    @NonNull
    Set<CityDAO> findAll();
    Optional<CityDAO> findByName(String cityName);

    Optional<CityDAO> findCityDAOByCountryDAO_IdAndCapitalIsTrue(int countryId);

    boolean existsByCountryDAO_IdAndName(int countryId, String name);
}

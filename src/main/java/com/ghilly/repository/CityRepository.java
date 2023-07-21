package com.ghilly.repository;

import com.ghilly.model.City;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Integer> {

    Optional<City> findByName(String cityName);

}

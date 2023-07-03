package com.ghilly.repository;

import com.ghilly.model.Countries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Countries, Integer> {
    Optional<Countries> findByName(String countryName);
}

package com.ghilly.repository;

import com.ghilly.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    Optional<Country> findByName(String countryName);
}

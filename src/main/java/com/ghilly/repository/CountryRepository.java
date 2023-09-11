package com.ghilly.repository;

import com.ghilly.model.dao.CountryDAO;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CountryRepository extends CrudRepository<CountryDAO, Integer> {
    Optional<CountryDAO> findByName(String countryName);

    @Override
    @NonNull
    Set<CountryDAO> findAll();
}
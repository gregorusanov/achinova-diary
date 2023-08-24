package com.ghilly.repository;

import com.ghilly.model.DAO.CountryDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<CountryDAO, Integer> {
    Optional<CountryDAO> findByName(String countryName);
}
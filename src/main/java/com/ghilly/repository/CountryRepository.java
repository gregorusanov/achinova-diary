package com.ghilly.repository;

import com.ghilly.model.dao.CountryEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CountryRepository extends CrudRepository<CountryEntity, Integer> {
    Optional<CountryEntity> findByName(String countryName);

    @Override
    @NonNull
    Set<CountryEntity> findAll();
}
package com.ghilly.repository;

import com.ghilly.model.dao.CityEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, Integer> {

    @Override
    @NonNull
    Set<CityEntity> findAll();
    Optional<CityEntity> findByName(String cityName);
    Optional<CityEntity> findCityEntityByCountryEntity_IdAndCapitalIsTrue(int countryId);

    boolean existsByCountryEntity_IdAndName(int countryId, String name);
}

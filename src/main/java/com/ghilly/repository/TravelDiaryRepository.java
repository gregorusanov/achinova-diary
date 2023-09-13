package com.ghilly.repository;

import com.ghilly.model.dao.TravelDiaryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TravelDiaryRepository extends CrudRepository<TravelDiaryEntity, Integer> {
    Optional<TravelDiaryEntity> findById(int id);
}

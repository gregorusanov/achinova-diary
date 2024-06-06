package com.ghilly.repository;

import com.ghilly.model.dao.TravelDiaryEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface TravelDiaryRepository extends CrudRepository<TravelDiaryEntity, Integer> {
    @Override
    @NonNull
    Set<TravelDiaryEntity> findAll();
}

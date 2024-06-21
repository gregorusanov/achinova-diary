package com.ghilly.repository;

import com.ghilly.model.dao.TravelDiaryEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;


@Repository
public interface TravelDiaryRepository extends CrudRepository<TravelDiaryEntity, Integer> {
    @Override
    @NonNull
    Set<TravelDiaryEntity> findAll();

    @NonNull
    Set<TravelDiaryEntity> findTravelDiaryEntitiesByArrivalDate(LocalDate localDate);

    TravelDiaryEntity findTravelDiaryEntityByDescription(String description);
}

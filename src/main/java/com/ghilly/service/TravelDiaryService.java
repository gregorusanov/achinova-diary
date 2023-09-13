package com.ghilly.service;

import com.ghilly.model.dao.TravelDiaryEntity;

import java.util.Optional;

public interface TravelDiaryService {
    TravelDiaryEntity create(TravelDiaryEntity travelDiaryEntity);

    Optional<TravelDiaryEntity> getTravelDiaryEntityById(int id);
}

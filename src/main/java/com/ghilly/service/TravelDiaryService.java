package com.ghilly.service;

import com.ghilly.model.dao.TravelDiaryEntity;
import java.util.Set;

public interface TravelDiaryService {
    TravelDiaryEntity create(TravelDiaryEntity travelDiaryEntity);

    TravelDiaryEntity getTravelDiaryEntityById(int id);

    Set<TravelDiaryEntity> getAll();

    void delete(int id);

    TravelDiaryEntity update(TravelDiaryEntity travelDiaryEntity);
}

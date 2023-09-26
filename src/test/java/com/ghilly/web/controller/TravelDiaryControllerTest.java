package com.ghilly.web.controller;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CityTravelDiaryCompositeKey;
import com.ghilly.model.dao.CityTravelDiaryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.model.dto.TravelDiary;
import com.ghilly.transformer.EntityTransformer;
import com.ghilly.web.handler.TravelDiaryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class TravelDiaryControllerTest {
    private final int cityId = 1;
    private Set<Integer> cityIdSet = Set.of(cityId);
    private CityEntity cityEntity = new CityEntity(cityId, "Rostov");
    private TravelDiaryHandler handler;
    private TravelDiaryController controller;

    @BeforeEach
    void init() {
        handler = mock(TravelDiaryHandler.class);
        controller = new TravelDiaryController(handler);
    }

    @Test
    void create() {
        TravelDiary travelDiaryDTO = new TravelDiary(1, "09.03.2022", "10.03.2022",
                800, 1000, "Home.", 8, cityIdSet);
        TravelDiaryEntity travelDiaryEntity = EntityTransformer.transformToTravelDiaryEntity(travelDiaryDTO);
        controller.create(travelDiaryDTO);

        assertAll(
                () -> verify(handler).create(travelDiaryEntity, cityIdSet),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void getOneRecordById() {
        TravelDiary travelDiaryDTO = new TravelDiary(1, "09.03.2022", "10.03.2022",
                800, 1000, "Home.", 8, cityIdSet);
        TravelDiaryEntity travelDiaryEntity = EntityTransformer.transformToTravelDiaryEntity(travelDiaryDTO);
        Set<CityTravelDiaryEntity> cityTravelDiarySet = new HashSet<>(Set.of(CityTravelDiaryEntity.builder()
                .id(new CityTravelDiaryCompositeKey())
                .travelDiaryEntity(travelDiaryEntity)
                .cityEntity(cityEntity)
                .build()));
        travelDiaryEntity.setCityTravelSet(cityTravelDiarySet);
        when(handler.getTravelDiaryEntityById(1)).thenReturn(Optional.of(travelDiaryEntity));
        controller.getTravelById(1);

        assertAll(
                () -> verify(handler).getTravelDiaryEntityById(1),
                () -> verifyNoMoreInteractions(handler)
        );
    }
}
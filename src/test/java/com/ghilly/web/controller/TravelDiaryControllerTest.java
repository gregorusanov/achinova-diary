package com.ghilly.web.controller;

import com.ghilly.model.dao.*;
import com.ghilly.model.dto.TravelDiary;
import com.ghilly.transformer.EntityTransformer;
import com.ghilly.web.handler.TravelDiaryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class TravelDiaryControllerTest {
    private final int cityId = 1;
    private final Set<Integer> cityIdSet = Set.of(cityId);
    private final TravelDiary travelDiaryDTO = TravelDiary.builder()
            .id(1)
            .arrivalDate("09.03.2022")
            .departureDate("10.03.2022")
            .plannedBudget(800)
            .realBudget(1000)
            .description("Home.")
            .cityIdSet(cityIdSet)
            .build();
    private final TravelDiaryEntity travelDiaryEntity = EntityTransformer.transformToTravelDiaryEntity(travelDiaryDTO);
    private final CountryEntity country = new CountryEntity(1, "Russia");
    private final CityEntity cityEntity = CityEntity.builder().id(1).name("Rostov").countryEntity(country).build();
    private final Set<CityTravelDiaryEntity> cityTravelDiarySet = new HashSet<>(Set.of(CityTravelDiaryEntity.builder()
            .id(new CityTravelDiaryCompositeKey())
            .travelDiaryEntity(travelDiaryEntity)
            .cityEntity(cityEntity)
            .build()));
    private TravelDiaryHandler handler;
    private TravelDiaryController controller;

    @BeforeEach
    void init() {
        handler = mock(TravelDiaryHandler.class);
        controller = new TravelDiaryController(handler);
    }

    @Test
    void create() {
        TravelDiaryEntity travelDiaryEntity = EntityTransformer.transformToTravelDiaryEntity(travelDiaryDTO);
        controller.create(travelDiaryDTO);

        assertAll(
                () -> verify(handler).create(travelDiaryEntity, cityIdSet),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void getOneRecordById() {
        travelDiaryEntity.setCityTravelSet(cityTravelDiarySet);

        when(handler.getTravelDiaryEntityById(1)).thenReturn(travelDiaryEntity);
        controller.getTravelById(1);

        assertAll(
                () -> verify(handler).getTravelDiaryEntityById(1),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void getAllRecords() {
        TravelDiary secondTravelDiaryDTO = TravelDiary.builder()
                .id(2)
                .arrivalDate("15.03.2022")
                .departureDate("20.03.2022")
                .plannedBudget(1000)
                .realBudget(1000)
                .description("Second record.")
                .cityIdSet(cityIdSet)
                .build();
        TravelDiary thirdTravelDiaryDTO = TravelDiary.builder()
                .id(3)
                .arrivalDate("16.03.2022")
                .departureDate("17.03.2022")
                .plannedBudget(500)
                .realBudget(600)
                .description("Third record.")
                .cityIdSet(Set.of(3))
                .build();
        CityEntity secondCity = CityEntity.builder()
                .id(3)
                .name("Volgograd")
                .countryEntity(country)
                .build();
        Set<CityTravelDiaryEntity> setForThirdTDDTO = Set.of(CityTravelDiaryEntity.builder()
                .id(new CityTravelDiaryCompositeKey())
                .travelDiaryEntity(EntityTransformer.transformToTravelDiaryEntity(thirdTravelDiaryDTO))
                .cityEntity(secondCity)
                .build());
        travelDiaryEntity.setCityTravelSet(cityTravelDiarySet);
        TravelDiaryEntity secondTDEntity = EntityTransformer.transformToTravelDiaryEntity(secondTravelDiaryDTO);
        TravelDiaryEntity thirdTDEntity = EntityTransformer.transformToTravelDiaryEntity(thirdTravelDiaryDTO);
        thirdTDEntity.setCityTravelSet(setForThirdTDDTO);
        Set<TravelDiaryEntity> result = Set.of(travelDiaryEntity, secondTDEntity, thirdTDEntity);
        when(handler.getAll()).thenReturn(result);

        controller.getAll();

        assertAll(
                () -> verify(handler).getAll(),
                () -> verifyNoMoreInteractions(handler)
        );
    }

    @Test
    void delete() {
        controller.delete(1);

        assertAll(
                () -> verify(handler).delete(1),
                () -> verifyNoMoreInteractions(handler)
        );
    }
}
package com.ghilly.web.controller;

import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.model.dto.TravelDiary;
import com.ghilly.transformer.EntityTransformer;
import com.ghilly.web.handler.TravelDiaryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class TravelDiaryControllerTest {
    private final int cityId = 1;
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
                800, 1000, "Home.", 8, cityId);
        TravelDiaryEntity travelDiaryEntity = EntityTransformer.transformToTravelDiaryEntity(travelDiaryDTO);
        controller.create(travelDiaryDTO);

        assertAll(
                () -> verify(handler).create(travelDiaryEntity, cityId),
                () -> verifyNoMoreInteractions(handler)
        );
    }
}
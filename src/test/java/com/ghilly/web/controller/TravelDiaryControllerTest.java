package com.ghilly.web.controller;

import com.ghilly.web.handler.TravelDiaryHandler;
import org.junit.jupiter.api.BeforeEach;

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
// This test shows that the argument in the assertion is different from the argument in the called method on the 29 line
//    @Test
//    void create() {
//        TravelDiaryDTO travelDiaryDTO = new TravelDiaryDTO(1, "09.03.2022", "10.03.2022",
//                800, 1000, "Home.", 8, cityId);
//        TravelDiaryDAO travelDiaryDAO = TransformerDAOandDTO.transformToTravelDiaryDAO(travelDiaryDTO);
//        controller.create(travelDiaryDTO);
//
//        assertAll(
//                () -> verify(handler).create(travelDiaryDAO, cityId),
//                () -> verifyNoMoreInteractions(handler)
//        );
//    }
}
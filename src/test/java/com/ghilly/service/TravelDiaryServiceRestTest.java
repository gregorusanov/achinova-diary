package com.ghilly.service;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CityTravelDiaryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.TravelDiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TravelDiaryServiceRestTest {
    private CityRepository cityRepository;
    private TravelDiaryRepository travelDiaryRepository;
    private TravelDiaryServiceRest service;
    private final CityTravelDiaryEntity cityTravelDiary = new CityTravelDiaryEntity();
    private final Set<CityTravelDiaryEntity> cityTravelDiarySet = new HashSet<>(Set.of(cityTravelDiary));
    private final CityEntity city = new CityEntity(2, "Netherlands");

    @BeforeEach
    void init() {
        cityRepository = mock(CityRepository.class);
        travelDiaryRepository = mock(TravelDiaryRepository.class);
        service = new TravelDiaryServiceRest(cityRepository, travelDiaryRepository);
    }

    private LocalDate parsingDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    @Test
    void create() {
        cityTravelDiary.setCityEntity(city);
        LocalDate arrivalDate = parsingDate("10.03.2023");
        LocalDate departureDate = parsingDate("12.03.2023");
        TravelDiaryEntity record = new TravelDiaryEntity(1, arrivalDate, departureDate, 800.00,
                1000.00, "Cold place.", 8, cityTravelDiarySet);
        cityTravelDiary.setTravelDiaryEntity(record);

        service.create(record);

        assertAll(
                () -> verify(travelDiaryRepository).save(record),
                () -> verifyNoMoreInteractions(travelDiaryRepository)
        );
    }
}
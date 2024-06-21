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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class TravelDiaryServiceRestTest {
    private CityRepository cityRepository;
    private TravelDiaryRepository travelDiaryRepository;
    private TravelDiaryServiceRest service;
    private final CityEntity city = CityEntity.builder().id(2).name("Berlin").build();
    private final CityTravelDiaryEntity cityTravelDiary = CityTravelDiaryEntity.builder().cityEntity(city).build();
    private final Set<CityTravelDiaryEntity> cityTravelDiarySet = new HashSet<>(Set.of(cityTravelDiary));
    private LocalDate arrivalDate = parsingDate("28.01.2023");
    private LocalDate departureDate = parsingDate("08.05.2024");
    private final TravelDiaryEntity travelDiaryEntity = TravelDiaryEntity.builder()
            .id(1)
            .arrivalDate(arrivalDate)
            .departureDate(departureDate)
            .plannedBudget(15000)
            .realBudget(15000)
            .description("The new beginning.")
            .rating(10)
            .cityTravelSet(cityTravelDiarySet)
            .build();

    @BeforeEach
    void init() {
        cityTravelDiary.setTravelDiaryEntity(travelDiaryEntity);
        cityRepository = mock(CityRepository.class);
        travelDiaryRepository = mock(TravelDiaryRepository.class);
        service = new TravelDiaryServiceRest(cityRepository, travelDiaryRepository);
    }

    private LocalDate parsingDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    @Test
    void create() {
        arrivalDate = parsingDate("10.03.2023");
        departureDate = parsingDate("12.03.2023");
        travelDiaryEntity.setPlannedBudget(800.00);
        travelDiaryEntity.setRealBudget(1000.00);
        travelDiaryEntity.setRating(8);
        travelDiaryEntity.setDescription("The place feels like a frozen island.");

        service.create(travelDiaryEntity);

        assertAll(
                () -> verify(travelDiaryRepository).save(travelDiaryEntity),
                () -> verifyNoMoreInteractions(travelDiaryRepository)
        );
    }

    @Test
    void getTravelDiary() {
        when(travelDiaryRepository.findById(1)).thenReturn(Optional.of(travelDiaryEntity));

        service.getTravelDiaryEntityById(1);

        assertAll(
                () -> verify(travelDiaryRepository).findById(1),
                () -> verifyNoMoreInteractions(travelDiaryRepository)
        );
    }

    @Test
    void getAll() {
        service.getAll();

        assertAll(
                () -> verify(travelDiaryRepository).findAll(),
                () -> verifyNoMoreInteractions(travelDiaryRepository)
        );
    }

    @Test
    void delete() {
        service.delete(10);

        assertAll(
                () -> verify(travelDiaryRepository).deleteById(10),
                () -> verifyNoMoreInteractions(travelDiaryRepository)
        );
    }
}
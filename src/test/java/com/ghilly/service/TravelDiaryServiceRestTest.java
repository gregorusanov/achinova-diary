package com.ghilly.service;

import com.ghilly.model.dao.CityDAO;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.TravelDiaryRepository;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class TravelDiaryServiceRestTest {
    private CityRepository cityRepository;
    private TravelDiaryRepository travelDiaryRepository;
    private TravelDiaryServiceRest service;
    private HashSet<CityDAO> cities = new HashSet<>();
    private CityDAO city = new CityDAO(2, "Netherlands");

    @BeforeEach
    void init() {
        cityRepository = mock(CityRepository.class);
        travelDiaryRepository = mock(TravelDiaryRepository.class);
        service = new TravelDiaryServiceRest(cityRepository, travelDiaryRepository);
    }

    private LocalDate parsingDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


//    @Test
//    void create() {
//        cities.add(city);
//        LocalDate arrivalDate = parsingDate("10.03.2023");
//        LocalDate departureDate = parsingDate("12.03.2023");
//        TravelDiaryDAO record = new TravelDiaryDAO(1, arrivalDate, departureDate, 800.0D, 1000.0D,
//                "Cold place.", 8, cities);
//
//        service.create(record);
//
//        assertAll(
//                () -> verify(travelDiaryRepository).save(record),
//                () -> verifyNoMoreInteractions(travelDiaryRepository)
//        );
//    }
}
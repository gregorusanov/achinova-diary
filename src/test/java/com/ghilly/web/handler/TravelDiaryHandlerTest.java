package com.ghilly.web.handler;

import com.ghilly.exception.*;
import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CityTravelDiaryEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.TravelDiaryServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelDiaryHandlerTest {
    private final String name = "Copenhagen";
    private int cityId = 1;
    private final CityEntity city = new CityEntity(cityId, name, new CountryEntity("Denmark"), true);
    private CityTravelDiaryEntity cityTravelDiary = new CityTravelDiaryEntity();
    private Set<CityTravelDiaryEntity> cityTravelDiarySet = new HashSet<>(Set.of(cityTravelDiary));
    private TravelDiaryHandler handler;
    private TravelDiaryServiceRest travelDiaryServiceRest;
    private CityServiceRest cityServiceRest;

    @BeforeEach
    void init() {
        travelDiaryServiceRest = mock(TravelDiaryServiceRest.class);
        cityServiceRest = mock(CityServiceRest.class);
        handler = new TravelDiaryHandler(cityServiceRest, travelDiaryServiceRest);
    }

    private LocalDate parsingDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void createSuccess() {
        when(cityServiceRest.cityIdExists(cityId)).thenReturn(true);
        when(cityServiceRest.getCity(cityId)).thenReturn(city);
        LocalDate arrivalDate = parsingDate("10.03.2023");
        LocalDate departureDate = parsingDate("12.03.2023");
        TravelDiaryEntity record = new TravelDiaryEntity(1, arrivalDate, departureDate, 800, 1000,
                "Cold place.", 8, cityTravelDiarySet);
        cityTravelDiary.setCityEntity(city);
        cityTravelDiary.setTravelDiaryEntity(record);

        handler.create(record, cityId);

        assertAll(
                () -> verify(cityServiceRest).cityIdExists(cityId),
                () -> verify(cityServiceRest).getCity(cityId),
                () -> verify(travelDiaryServiceRest).create(record),
                () -> verifyNoMoreInteractions(travelDiaryServiceRest, cityServiceRest)
        );
    }

    @Test
    void createWithIllegalDate() {
        cityId++;
        LocalDate arrivalDate = parsingDate("10.03.2023");
        LocalDate departureDate = parsingDate("09.03.2023");
        TravelDiaryEntity record = new TravelDiaryEntity(1, arrivalDate, departureDate, 800, 1000,
                "Cold place.", 8, cityTravelDiarySet);

        IllegalDateException e = assertThrows(IllegalDateException.class, () -> handler.create(record, cityId));

        assertAll(
                () -> assertEquals("The arrival date should be " +
                        "earlier than departure date or should be equal to it! Arrival date: "
                        + arrivalDate + ". Departure date: " + departureDate, e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createWithIllegalBudget() {
        cityId = cityId * 3;
        LocalDate arrivalDate = parsingDate("10.03.2023");
        LocalDate departureDate = parsingDate("12.03.2023");
        TravelDiaryEntity record = new TravelDiaryEntity(1, arrivalDate, departureDate, 800, -1,
                "Cold place.", 8, cityTravelDiarySet);

        IllegalBudgetException e = assertThrows(IllegalBudgetException.class, () -> handler.create(record, cityId));

        assertAll(
                () -> assertEquals("The budget should not be less than 0. Wrong budget: " + -1.0, e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createWithIllegalRating() {
        LocalDate arrivalDate = parsingDate("10.03.2023");
        LocalDate departureDate = parsingDate("12.03.2023");
        TravelDiaryEntity record = new TravelDiaryEntity(1, arrivalDate, departureDate, 800, 1000,
                "Cold place.", 11, cityTravelDiarySet);

        IllegalRatingNumberException e = assertThrows(IllegalRatingNumberException.class,
                () -> handler.create(record, cityId));

        assertAll(
                () -> assertEquals("The rating should be in the range from 0 to 10, " +
                        "including these numbers. Wrong rating: " + record.getRating(), e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createWithLongDescription() {
        LocalDate arrivalDate = parsingDate("10.03.2023");
        LocalDate departureDate = parsingDate("12.03.2023");
        TravelDiaryEntity record = new TravelDiaryEntity(1, arrivalDate, departureDate, 800, 1000,
                "a".repeat(301), 10, cityTravelDiarySet);

        TooLongDescriptionException e = assertThrows(TooLongDescriptionException.class,
                () -> handler.create(record, cityId));

        assertAll(
                () -> assertEquals("The description should be no longer than 300 symbols, including spaces.",
                        e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createFailCityIdNotFound() {
        LocalDate arrivalDate = parsingDate("10.03.2023");
        LocalDate departureDate = parsingDate("12.03.2023");
        TravelDiaryEntity record = new TravelDiaryEntity(1, arrivalDate, departureDate, 800, 1000,
                "Cold place.", 8, cityTravelDiarySet);

        assertThrows(IdNotFoundException.class, () -> handler.create(record, cityId));

        assertAll(
                () -> verify(cityServiceRest).cityIdExists(cityId),
                () -> verifyNoMoreInteractions(cityServiceRest),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }
}
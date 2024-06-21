package com.ghilly.web.handler;

import com.ghilly.exception.*;
import com.ghilly.model.dao.*;
import com.ghilly.model.dto.TravelDiary;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.TravelDiaryServiceRest;
import com.ghilly.transformer.EntityTransformer;
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
    private final int cityId = 1;
    private final Set<Integer> cityIdSet = Set.of(cityId);
    private final CityEntity city = CityEntity.builder().id(cityId).name(name)
            .countryEntity(new CountryEntity("Denmark")).capital(true).build();
    private final CityTravelDiaryEntity cityTravelDiary = CityTravelDiaryEntity.builder()
            .id(new CityTravelDiaryCompositeKey())
            .cityEntity(city)
            .build();
    private final Set<CityTravelDiaryEntity> cityTravelDiarySet = new HashSet<>(Set.of(cityTravelDiary));
    private final LocalDate arrivalDate = parsingDate("10.03.2023");
    private final LocalDate departureDate = parsingDate("12.03.2023");
    private final TravelDiaryEntity travelDiaryEntity = TravelDiaryEntity.builder()
            .id(1)
            .arrivalDate(arrivalDate)
            .departureDate(departureDate)
            .plannedBudget(800)
            .realBudget(1000)
            .description("Cold place.")
            .rating(8)
            .cityTravelSet(cityTravelDiarySet)
            .build();
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
        cityTravelDiary.setCityEntity(city);
        cityTravelDiary.setTravelDiaryEntity(travelDiaryEntity);

        handler.create(travelDiaryEntity, cityIdSet);

        assertAll(
                () -> verify(cityServiceRest).cityIdExists(cityId),
                () -> verify(cityServiceRest).getCity(cityId),
                () -> verify(travelDiaryServiceRest).create(travelDiaryEntity),
                () -> verifyNoMoreInteractions(travelDiaryServiceRest, cityServiceRest)
        );
    }

    @Test
    void createWithIllegalDate() {
        LocalDate wrongDate = parsingDate("05.05.2012");
        travelDiaryEntity.setDepartureDate(wrongDate);

        IllegalDateException e = assertThrows(IllegalDateException.class,
                () -> handler.create(travelDiaryEntity, cityIdSet));

        assertAll(
                () -> assertEquals("The arrival date should be " +
                        "earlier than departure date or should be equal to it! Arrival date: "
                        + arrivalDate + ". Departure date: " + wrongDate, e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createWithIllegalBudget() {
        travelDiaryEntity.setId(cityId * 3);
        travelDiaryEntity.setRealBudget(-100);

        IllegalBudgetException e = assertThrows(IllegalBudgetException.class,
                () -> handler.create(travelDiaryEntity, cityIdSet));

        assertAll(
                () -> assertEquals("The budget should not be less than 0. Wrong budget: " + -100.0, e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createWithIllegalRating() {
        travelDiaryEntity.setRating(100500);

        IllegalRatingNumberException e = assertThrows(IllegalRatingNumberException.class,
                () -> handler.create(travelDiaryEntity, cityIdSet));

        assertAll(
                () -> assertEquals("The rating should be in the range from 0 to 10, " +
                        "including these numbers. Wrong rating: " + travelDiaryEntity.getRating(), e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createWithLongDescription() {
        travelDiaryEntity.setDescription("a".repeat(301));

        TooLongDescriptionException e = assertThrows(TooLongDescriptionException.class,
                () -> handler.create(travelDiaryEntity, cityIdSet));

        assertAll(
                () -> assertEquals("The description should be no longer than 300 symbols, including spaces.",
                        e.getMessage()),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void createFailCityIdNotFound() {
        assertThrows(IdNotFoundException.class, () -> handler.create(travelDiaryEntity, cityIdSet));

        assertAll(
                () -> verify(cityServiceRest).cityIdExists(cityId),
                () -> verifyNoMoreInteractions(cityServiceRest),
                () -> verifyNoInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void getTravelDiarySuccess() {
        cityTravelDiary.setTravelDiaryEntity(travelDiaryEntity);

        when(travelDiaryServiceRest.travelIdExists(1)).thenReturn(true);
        when(travelDiaryServiceRest.getTravelDiaryEntityById(1)).thenReturn(travelDiaryEntity);

        handler.getTravelDiaryEntityById(1);

        assertAll(
                () -> verify(travelDiaryServiceRest).travelIdExists(1),
                () -> verify(travelDiaryServiceRest).getTravelDiaryEntityById(1),
                () -> verifyNoMoreInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void getTravelDiaryIdNotFound() {
        IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> handler.getTravelDiaryEntityById(1));

        assertAll(
                () -> assertEquals("The travel ID " + 1 + " is not found.", e.getMessage()),
                () -> verify(travelDiaryServiceRest).travelIdExists(1),
                () -> verifyNoMoreInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void getAll() {
        TravelDiary thirdTravelDiaryDTO = TravelDiary.builder()
                .id(3)
                .arrivalDate("16.09.2023")
                .departureDate("17.09.2023")
                .plannedBudget(500)
                .realBudget(600)
                .description("Third record.")
                .cityIdSet(Set.of(3))
                .build();
        CityEntity secondCity = CityEntity.builder()
                .id(3)
                .name("Rotterdam")
                .countryEntity(new CountryEntity("Netherlands"))
                .build();
        Set<CityTravelDiaryEntity> setForThirdTDDTO = Set.of(CityTravelDiaryEntity.builder()
                .id(new CityTravelDiaryCompositeKey())
                .travelDiaryEntity(EntityTransformer.transformToTravelDiaryEntity(thirdTravelDiaryDTO))
                .cityEntity(secondCity)
                .build());
        travelDiaryEntity.setCityTravelSet(cityTravelDiarySet);
        TravelDiaryEntity secondTDEntity = EntityTransformer.transformToTravelDiaryEntity(thirdTravelDiaryDTO);
        secondTDEntity.setCityTravelSet(setForThirdTDDTO);
        Set<TravelDiaryEntity> result = Set.of(travelDiaryEntity, secondTDEntity);

        when(travelDiaryServiceRest.getAll()).thenReturn(result);

        handler.getAll();

        assertAll(
                () -> verify(travelDiaryServiceRest).getAll(),
                () -> verifyNoMoreInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void deleteSuccess() {
        when(travelDiaryServiceRest.travelIdExists(100)).thenReturn(true);

        handler.delete(100);

        assertAll(
                () -> verify(travelDiaryServiceRest).travelIdExists(100),
                () -> verify(travelDiaryServiceRest).delete(100),
                () -> verifyNoMoreInteractions(travelDiaryServiceRest)
        );
    }

    @Test
    void deleteIdNotFound() {
        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> handler.delete(40));

        assertAll(
                () -> assertEquals(exception.getMessage(), "The travel ID " + 40 + " is not found."),
                () -> verify(travelDiaryServiceRest).travelIdExists(40),
                () -> verifyNoMoreInteractions(travelDiaryServiceRest)
        );
    }

}
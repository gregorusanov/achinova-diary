package com.ghilly.transformer;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.model.dto.City;
import com.ghilly.model.dto.Country;
import com.ghilly.model.dto.TravelDiary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class EntityTransformer {
    private EntityTransformer() {
    }

    public static CountryEntity transformToCountryEntity(Country country) {
        return CountryEntity.builder()
                .id(country.getId())
                .name(country.getName())
                .citySet(new HashSet<>())
                .build();
    }

    public static Country transformToCountry(CountryEntity countryEntity) {
        return Country.builder()
                .id(countryEntity.getId())
                .name(countryEntity.getName())
                .build();
    }

    public static City transformToCity(CityEntity cityEntity) {
        return City.builder()
                .id(cityEntity.getId())
                .name(cityEntity.getName())
                .countryId(cityEntity.getCountryEntity().getId())
                .capital(cityEntity.isCapital())
                .build();
    }

    public static CityEntity transformToCityEntity(City city) {
        return CityEntity.builder()
                .id(city.getId())
                .name(city.getName())
                .countryEntity(null)
                .capital(city.isCapital())
                .build();
    }

    public static TravelDiaryEntity transformToTravelDiaryEntity(TravelDiary travelDiary) {
        String pattern = "dd.MM.yyyy";
        LocalDate arrivalDate = LocalDate.parse(travelDiary.getArrivalDate(), DateTimeFormatter.ofPattern(pattern));
        LocalDate departureDate = LocalDate.parse(travelDiary.getDepartureDate(), DateTimeFormatter.ofPattern(pattern));
        return TravelDiaryEntity.builder()
                .id(travelDiary.getId())
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .plannedBudget(travelDiary.getPlannedBudget())
                .realBudget(travelDiary.getRealBudget())
                .description(travelDiary.getDescription())
                .rating(travelDiary.getRating())
                .cityTravelSet(new HashSet<>())
                .build();
    }

    public static TravelDiary transformToTravelDiary(TravelDiaryEntity travelDiaryEntity) {
        return TravelDiary.builder()
                .id(travelDiaryEntity.getId())
                .arrivalDate(travelDiaryEntity.getArrivalDate().toString())
                .departureDate(travelDiaryEntity.getDepartureDate().toString())
                .plannedBudget(travelDiaryEntity.getPlannedBudget())
                .realBudget(travelDiaryEntity.getRealBudget())
                .description(travelDiaryEntity.getDescription())
                .rating(travelDiaryEntity.getRating())
                .cityId(travelDiaryEntity.getCityTravelSet().stream().findFirst().orElseThrow().getCityEntity().getId())
                .build();

    }
}

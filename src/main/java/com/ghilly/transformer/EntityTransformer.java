package com.ghilly.transformer;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CityTravelDiaryEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.model.dto.City;
import com.ghilly.model.dto.Country;
import com.ghilly.model.dto.TravelDiary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityTransformer {
    private EntityTransformer() {
    }

    public static CountryEntity transformToCountryEntity(Country country) {
        return new CountryEntity(country.getId(), country.getName(), new HashSet<>());
    }

    public static Country transformToCountry(CountryEntity countryEntity) {
        return new Country(countryEntity.getId(), countryEntity.getName());
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
        Set<Integer> cities = travelDiaryEntity.getCityTravelSet().stream()
                .map((CityTravelDiaryEntity::getCityEntity))
                .map(CityEntity::getId)
                .collect(Collectors.toSet());
        return TravelDiary.builder()
                .id(travelDiaryEntity.getId())
                .arrivalDate(travelDiaryEntity.getArrivalDate().toString())
                .departureDate(travelDiaryEntity.getDepartureDate().toString())
                .plannedBudget(travelDiaryEntity.getPlannedBudget())
                .realBudget(travelDiaryEntity.getRealBudget())
                .description(travelDiaryEntity.getDescription())
                .rating(travelDiaryEntity.getRating())
                .cityIdSet(cities)
                .build();

    }
}

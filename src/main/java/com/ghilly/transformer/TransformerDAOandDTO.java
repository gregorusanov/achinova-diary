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

public class TransformerDAOandDTO {
    private TransformerDAOandDTO() {
    }

    public static CountryEntity transformToCountryDAO(Country country) {
        return new CountryEntity(country.getId(), country.getName(), new HashSet<>());
    }

    public static Country transformToCountryDTO(CountryEntity countryEntity) {
        int id = countryEntity.getId();
        String name = countryEntity.getName();
        return new Country(id, name);
    }

    public static City transformToCityDTO(CityEntity cityEntity) {
        return new City(cityEntity.getId(), cityEntity.getName(), cityEntity.getCountryEntity().getId(), cityEntity.isCapital());
    }

    public static CityEntity transformToCityDAO(City city) {
        return new CityEntity(city.getId(), city.getName(), null, city.isCapital());
    }

    public static TravelDiaryEntity transformToTravelDiaryDAO(TravelDiary travelDiary) {
        String s = "dd.MM.yyyy";
        LocalDate arrivalDate = LocalDate.parse(travelDiary.getArrivalDate(), DateTimeFormatter.ofPattern(s));
        LocalDate departureDate = LocalDate.parse(travelDiary.getDepartureDate(), DateTimeFormatter.ofPattern(s));
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

    public static TravelDiary transformToTravelDiaryDTO(TravelDiaryEntity travelDiaryEntity) {
        return TravelDiary.builder()
                .id(travelDiaryEntity.getId())
                .arrivalDate(travelDiaryEntity.getArrivalDate().toString())
                .departureDate(travelDiaryEntity.getDepartureDate().toString())
                .plannedBudget(travelDiaryEntity.getPlannedBudget())
                .realBudget(travelDiaryEntity.getRealBudget())
                .description(travelDiaryEntity.getDescription())
                .rating(travelDiaryEntity.getRating())
                .cityId(travelDiaryEntity.getCityTravelSet().stream().findFirst().get().getCityEntity().getId())
                .build();

    }
}

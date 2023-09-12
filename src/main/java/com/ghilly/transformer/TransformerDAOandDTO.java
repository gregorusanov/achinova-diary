package com.ghilly.transformer;

import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.model.dto.CityDTO;
import com.ghilly.model.dto.CountryDTO;
import com.ghilly.model.dto.TravelDiaryDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class TransformerDAOandDTO {
    private TransformerDAOandDTO() {
    }

    public static CountryEntity transformToCountryDAO(CountryDTO countryDTO) {
        return new CountryEntity(countryDTO.getId(), countryDTO.getName(), new HashSet<>());
    }

    public static CountryDTO transformToCountryDTO(CountryEntity countryEntity) {
        int id = countryEntity.getId();
        String name = countryEntity.getName();
        return new CountryDTO(id, name);
    }

    public static CityDTO transformToCityDTO(CityEntity cityEntity) {
        return new CityDTO(cityEntity.getId(), cityEntity.getName(), cityEntity.getCountryEntity().getId(), cityEntity.isCapital());
    }

    public static CityEntity transformToCityDAO(CityDTO cityDTO) {
        return new CityEntity(cityDTO.getId(), cityDTO.getName(), null, cityDTO.isCapital());
    }

    public static TravelDiaryEntity transformToTravelDiaryDAO(TravelDiaryDTO travelDiaryDTO) {
        String s = "dd.MM.yyyy";
        LocalDate arrivalDate = LocalDate.parse(travelDiaryDTO.getArrivalDate(), DateTimeFormatter.ofPattern(s));
        LocalDate departureDate = LocalDate.parse(travelDiaryDTO.getDepartureDate(), DateTimeFormatter.ofPattern(s));
        return TravelDiaryEntity.builder()
                .id(travelDiaryDTO.getId())
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .plannedBudget(travelDiaryDTO.getPlannedBudget())
                .realBudget(travelDiaryDTO.getRealBudget())
                .description(travelDiaryDTO.getDescription())
                .rating(travelDiaryDTO.getRating())
                .cityTravelSet(new HashSet<>())
                .build();
    }

    public static TravelDiaryDTO transformToTravelDiaryDTO(TravelDiaryEntity travelDiaryEntity) {
        return TravelDiaryDTO.builder()
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

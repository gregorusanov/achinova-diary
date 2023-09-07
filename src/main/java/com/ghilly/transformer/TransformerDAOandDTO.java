package com.ghilly.transformer;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.model.DAO.TravelDiaryDAO;
import com.ghilly.model.DTO.CityDTO;
import com.ghilly.model.DTO.CountryDTO;
import com.ghilly.model.DTO.TravelDiaryDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;

public class TransformerDAOandDTO {
    private TransformerDAOandDTO() {
    }

    public static CountryDAO transformToCountryDAO(CountryDTO countryDTO) {
        return new CountryDAO(countryDTO.getId(), countryDTO.getName(), new ArrayList<>());
    }

    public static CountryDTO transformToCountryDTO(CountryDAO countryDAO) {
        int id = countryDAO.getId();
        String name = countryDAO.getName();
        return new CountryDTO(id, name);
    }

    public static CityDTO transformToCityDTO(CityDAO cityDAO) {
        return new CityDTO(cityDAO.getId(), cityDAO.getName(), cityDAO.getCountryDAO().getId(), cityDAO.isCapital());
    }

    public static CityDAO transformToCityDAO(CityDTO cityDTO) {
        return new CityDAO(cityDTO.getId(), cityDTO.getName(), null, cityDTO.isCapital());
    }

    public static TravelDiaryDAO transformToTravelDiaryDAO(TravelDiaryDTO travelDiaryDTO) {
        String s = "dd.MM.yyyy";
        LocalDate arrivalDate = LocalDate.parse(travelDiaryDTO.getArrivalDate(), DateTimeFormatter.ofPattern(s));
        LocalDate departureDate = LocalDate.parse(travelDiaryDTO.getDepartureDate(), DateTimeFormatter.ofPattern(s));
        return TravelDiaryDAO.builder()
                .id(travelDiaryDTO.getId())
                .arrivalDate(arrivalDate)
                .departureDate(departureDate)
                .plannedBudget(travelDiaryDTO.getPlannedBudget())
                .realBudget(travelDiaryDTO.getRealBudget())
                .description(travelDiaryDTO.getDescription())
                .rating(travelDiaryDTO.getRating())
                .cities(new HashSet<>())
                .build();
    }

    public static TravelDiaryDTO transformToTravelDiaryDTO(TravelDiaryDAO travelDiaryDAO) {
        return TravelDiaryDTO.builder()
                .id(travelDiaryDAO.getId())
                .arrivalDate(travelDiaryDAO.getArrivalDate().toString())
                .departureDate(travelDiaryDAO.getDepartureDate().toString())
                .plannedBudget(travelDiaryDAO.getPlannedBudget())
                .realBudget(travelDiaryDAO.getRealBudget())
                .description(travelDiaryDAO.getDescription())
                .rating(travelDiaryDAO.getRating())
                .build();
    }
}
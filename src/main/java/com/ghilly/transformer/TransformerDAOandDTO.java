package com.ghilly.transformer;

import com.ghilly.model.dao.CityDAO;
import com.ghilly.model.dao.CountryDAO;
import com.ghilly.model.dao.TravelDiaryDAO;
import com.ghilly.model.dto.CityDTO;
import com.ghilly.model.dto.CountryDTO;
import com.ghilly.model.dto.TravelDiaryDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class TransformerDAOandDTO {
    private TransformerDAOandDTO() {
    }

    public static CountryDAO transformToCountryDAO(CountryDTO countryDTO) {
        return new CountryDAO(countryDTO.getId(), countryDTO.getName(), new HashSet<>());
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
                .cityDAOSet(new HashSet<>())
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
                .cityId(travelDiaryDAO.getCityDAOSet().stream().findFirst().get().getCityDAO().getId())
                .build();

    }
}

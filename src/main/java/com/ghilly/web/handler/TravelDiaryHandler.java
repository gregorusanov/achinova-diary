package com.ghilly.web.handler;

import com.ghilly.exception.*;
import com.ghilly.model.dao.CityTravelDiaryCompositeKey;
import com.ghilly.model.dao.CityTravelDiaryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.TravelDiaryServiceRest;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Set;

public class TravelDiaryHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TravelDiaryHandler.class);
    private final TravelDiaryServiceRest travelDiaryService;
    private final CityServiceRest cityService;

    public TravelDiaryHandler(CityServiceRest cityService, TravelDiaryServiceRest travelDiaryService) {
        this.cityService = cityService;
        this.travelDiaryService = travelDiaryService;
    }

    public TravelDiaryEntity create(TravelDiaryEntity travelDiaryEntity, Set<Integer> citiesId) {
        checkData(travelDiaryEntity);
        citiesId.forEach((cityId) -> enrichEntity(travelDiaryEntity, cityId));
        logger.info("Transferring data {} to the service", travelDiaryEntity);
        return travelDiaryService.create(travelDiaryEntity);
    }

    public TravelDiaryEntity getTravelDiaryEntityById(int id) {
        logger.info("Checking city {} exists", id);
        checkTravelIdExists(id);
        return travelDiaryService.getTravelDiaryEntityById(id);
    }

    public Set<TravelDiaryEntity> getAll() {
        logger.info("Getting data from the service.");
        return travelDiaryService.getAll();
    }

    public void delete(int id) {
        logger.info("Checking record {} exists", id);
        checkTravelIdExists(id);
        travelDiaryService.delete(id);
    }

    private void checkData(TravelDiaryEntity travelDiaryEntity) {
        checkDate(travelDiaryEntity.getArrivalDate(), travelDiaryEntity.getDepartureDate());
        checkRating(travelDiaryEntity.getRating());
        checkBudget(travelDiaryEntity.getPlannedBudget());
        checkBudget(travelDiaryEntity.getRealBudget());
        checkDescriptionLength(travelDiaryEntity.getDescription());
    }

    private void enrichEntity(TravelDiaryEntity travelDiaryEntity, int cityId) {
        checkCityIdExists(cityId);
        CityTravelDiaryEntity cityTravelDiaryEntity = CityTravelDiaryEntity.builder()
                .id(new CityTravelDiaryCompositeKey())
                .cityEntity(cityService.getCity(cityId))
                .travelDiaryEntity(travelDiaryEntity)
                .build();
        travelDiaryEntity.getCityTravelSet().add(cityTravelDiaryEntity);
    }

    private void checkDate(LocalDate arrivalDate, LocalDate departureDate) {
        if (arrivalDate.isAfter(departureDate)) throw new IllegalDateException("The arrival date should be " +
                "earlier than departure date or should be equal to it! Arrival date: "
                + arrivalDate + ". Departure date: " + departureDate);
    }

    private void checkRating(int rating) {
        if ((rating < 0) | (rating > 10)) throw new IllegalRatingNumberException("The rating " +
                "should be in the range from 0 to 10, including these numbers. Wrong rating: " + rating);
    }

    private void checkBudget(double budget) {
        if (budget < 0) throw
                new IllegalBudgetException("The budget should not be less than 0. Wrong budget: " + budget);
    }

    private void checkDescriptionLength(String description) {
        if (description.length() > 300) throw new TooLongDescriptionException("The description " +
                "should be no longer than 300 symbols, including spaces.");
    }

    private void checkCityIdExists(int cityId) {
        if (!cityService.cityIdExists(cityId))
            throw new IdNotFoundException("The city ID " + cityId + " is not found.");
    }

    private void checkTravelIdExists(int id) {
        if (!travelDiaryService.travelIdExists(id))
            throw new IdNotFoundException("The travel ID " + id + " is not found.");
    }
}

package com.ghilly.web.handler;

import com.ghilly.exception.*;
import com.ghilly.model.DAO.TravelDiaryDAO;
import com.ghilly.service.CityServiceRest;
import com.ghilly.service.TravelDiaryServiceRest;
import com.ghilly.web.controller.CityController;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Set;

public class TravelDiaryHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final TravelDiaryServiceRest travelDiaryService;
    private final CityServiceRest cityService;

    public TravelDiaryHandler(CityServiceRest cityService, TravelDiaryServiceRest travelDiaryService) {
        this.cityService = cityService;
        this.travelDiaryService = travelDiaryService;
    }

    public TravelDiaryDAO create(TravelDiaryDAO travelDiaryDAO, int cityId) {
        checkBeforeCreating(travelDiaryDAO, cityId);
        logger.info("Transferring data {} to the service", travelDiaryDAO);
        return travelDiaryService.create(travelDiaryDAO);
    }

    private void checkBeforeCreating(TravelDiaryDAO travelDiaryDAO, int cityId) {
        checkDate(travelDiaryDAO.getArrivalDate(), travelDiaryDAO.getDepartureDate());
        checkRating(travelDiaryDAO.getRating());
        checkBudget(travelDiaryDAO.getPlannedBudget());
        checkBudget(travelDiaryDAO.getRealBudget());
        checkDescriptionLength(travelDiaryDAO.getDescription());
        checkCityIdExists(cityId);
        travelDiaryDAO.setCityDAOSet(Set.of(cityService.getCity(cityId)));
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
}

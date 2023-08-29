package com.ghilly.model.DTO;

import java.util.Date;

public class TravelDiaryDTO {
    private int id;
    private Date arrivalDate;
    private Date departureDate;
    private double plannedBudget;
    private double realBudget;
    private String description;
    private int rating;
    private int cityId;

    public TravelDiaryDTO(int id, Date arrivalDate, Date departureDate, double plannedBudget, double realBudget,
                          String description, int rating, int cityId) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.plannedBudget = plannedBudget;
        this.realBudget = realBudget;
        this.description = description;
        this.rating = rating;
        this.cityId = cityId;
    }

    public TravelDiaryDTO(int id, Date arrivalDate, Date departureDate) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public TravelDiaryDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public double getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(double plannedBudget) {
        this.plannedBudget = plannedBudget;
    }

    public double getRealBudget() {
        return realBudget;
    }

    public void setRealBudget(double realBudget) {
        this.realBudget = realBudget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}

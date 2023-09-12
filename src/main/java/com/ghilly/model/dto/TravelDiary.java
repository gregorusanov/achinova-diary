package com.ghilly.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TravelDiary {
    private int id;
    private String arrivalDate;
    private String departureDate;
    private double plannedBudget;
    private double realBudget;
    private String description;
    private int rating;
    private int cityId;

    public TravelDiary(int id, String arrivalDate, String departureDate) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }
}

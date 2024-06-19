package com.ghilly.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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
    private Set<Integer> cityIdSet = new HashSet<>();
}

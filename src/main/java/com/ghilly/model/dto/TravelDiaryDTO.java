package com.ghilly.model.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TravelDiaryDTO {
    private int id;
    private String arrivalDate;
    private String departureDate;
    private double plannedBudget;
    private double realBudget;
    private String description;
    private int rating;
    private int cityId;

    public TravelDiaryDTO(int id, String arrivalDate, String departureDate) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }
}

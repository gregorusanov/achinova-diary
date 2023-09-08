package com.ghilly.model.DTO;

import lombok.*;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TravelDiaryDTO {
    private int id;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private double plannedBudget;
    private double realBudget;
    private String description;
    private int rating;
    private int cityId;

    public TravelDiaryDTO(int id, LocalDate arrivalDate, LocalDate departureDate) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }
}

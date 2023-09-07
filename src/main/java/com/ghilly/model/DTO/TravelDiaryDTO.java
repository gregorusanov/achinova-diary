package com.ghilly.model.DTO;
import lombok.*;


@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TravelDiaryDTO {
    private int id;

    private String arrivalDate;

    private String departureDate;

    private double plannedBudget;

    private double realBudget;

    private String description;

    private int rating;

    private int cityId;

    public TravelDiaryDTO(double plannedBudget, double realBudget, int rating, int cityId) {
        this.plannedBudget = plannedBudget;
        this.realBudget = realBudget;
        this.rating = rating;
        this.cityId = cityId;
    }
}

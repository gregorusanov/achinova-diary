package com.ghilly.model.DTO;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @Min(value = 0, message = "Minimal value for the field planned budget is 0.")
    private double plannedBudget;

    @Min(value = 0, message = "Minimal value for the field real budget is 0.")
    private double realBudget;

    @Size(max = 301, message = "The description should be in the range from 0 to 300 symbols.")
    private String description;

    @Min(value = 0, message = "Minimal value for the field rating is 0.")
    @Max(value = 10, message = "Maximal value for the field rating is 10.")
    private int rating;

    @NotNull
    private int cityId;

    public TravelDiaryDTO(double plannedBudget, double realBudget, int rating, int cityId) {
        this.plannedBudget = plannedBudget;
        this.realBudget = realBudget;
        this.rating = rating;
        this.cityId = cityId;
    }
}

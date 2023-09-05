package com.ghilly.model.DAO;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "travel_diary")
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelDiaryDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "travel_diary_seq")
    @SequenceGenerator(name = "travel_diary_seq", sequenceName = "travel_diary_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "planned_budget")
    @Min(value = 0, message = "Minimal value for the field planned budget is 0.")
    private double plannedBudget;

    @Column(name = "real_budget")
    @Min(value = 0, message = "Minimal value for the field real budget is 0.")
    private double realBudget;

    @Column(name = "description")
    @Size(max = 301, message = "The description should be in the range from 0 to 300 symbols.")
    private String description;

    @Column(name = "rating")
    @Min(value = 0, message = "Minimal value for the field rating is 0.")
    @Max(value = 10, message = "Maximal value for the field rating is 10.")
    private int rating;

    @ManyToMany(mappedBy = "travels")
    private Set<CityDAO> cities = new HashSet<>();

}

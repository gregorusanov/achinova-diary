package com.ghilly.model.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "travel_diary")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private double plannedBudget;

    @Column(name = "real_budget")
    private double realBudget;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private int rating;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "travelDiaryDAO", cascade = CascadeType.ALL)
    private Set<CityTravelDiaryDAO> cityDAOSet = new HashSet<>();

    //    @PreRemove
//    private void preRemove() {
//        cityDAOSet.forEach();
//    }
}

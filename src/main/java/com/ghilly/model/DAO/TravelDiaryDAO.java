package com.ghilly.model.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "travel_diary")
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private double plannedBudget;

    @Column(name = "real_budget")
    private double realBudget;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private int rating;

    @ManyToMany(mappedBy = "travelDiaryDAOSet", cascade = CascadeType.PERSIST)
    private Set<CityDAO> cityDAOSet;

    @PreRemove
    private void preRemove() {
        cityDAOSet.forEach(city -> city.getTravelDiaryDAOSet().remove(this));
    }

    public TravelDiaryDAO(int id, LocalDate arrivalDate, LocalDate departureDate, double plannedBudget, double realBudget,
                          String description, int rating) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.plannedBudget = plannedBudget;
        this.realBudget = realBudget;
        this.description = description;
        this.rating = rating;
    }

    public TravelDiaryDAO(int id, LocalDate arrivalDate, LocalDate departureDate) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }
}

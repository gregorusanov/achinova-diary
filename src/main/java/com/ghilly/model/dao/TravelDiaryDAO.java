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
    private double plannedBudget;

    public Set<CityTravelDiaryDAO> getCityDAOSet() {
        return cityDAOSet;
    }

    public void setCityDAOSet(Set<CityTravelDiaryDAO> cityDAOSet) {
        this.cityDAOSet = cityDAOSet;
    }

    @Column(name = "real_budget")
    private double realBudget;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private int rating;

    @OneToMany(mappedBy = "travelDiaryDAO", cascade = CascadeType.ALL)
    private Set<CityTravelDiaryDAO> cityDAOSet = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelDiaryDAO that)) return false;
        return id == that.id
                && Double.compare(that.plannedBudget, plannedBudget) == 0
                && Double.compare(that.realBudget, realBudget) == 0
                && rating == that.rating && Objects.equals(arrivalDate, that.arrivalDate)
                && Objects.equals(departureDate, that.departureDate)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, arrivalDate, departureDate, plannedBudget, realBudget, description, rating);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
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

    //    @PreRemove
//    private void preRemove() {
//        cityDAOSet.forEach();
//    }
}

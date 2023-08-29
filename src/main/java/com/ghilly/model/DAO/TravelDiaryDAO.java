package com.ghilly.model.DAO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "travel_diary")
public class TravelDiaryDAO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "travel_diary_seq")
    @SequenceGenerator(name = "travel_diary_seq", sequenceName = "travel_diary_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "arrival_date")
    @NotNull
    private Date arrivalDate;

    @Column(name = "departure_date")
    @NotNull
    private Date departureDate;

    @Column(name = "planned_budget")
    private double plannedBudget;

    @Column(name = "real_budget")
    private double realBudget;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private int rating;

    @ManyToMany()
    @JoinTable(name = "cities_travel_diary",
            joinColumns = @JoinColumn(name = "travel_diary_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "city_id", referencedColumnName = "id"))
    private List<CityDAO> cityDAOList;

    public TravelDiaryDAO(int id, Date arrivalDate, Date departureDate, double plannedBudget, double realBudget,
                          String description, int rating) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.plannedBudget = plannedBudget;
        this.realBudget = realBudget;
        this.description = description;
        this.rating = rating;
    }

    public TravelDiaryDAO(int id, Date arrivalDate, Date departureDate) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public TravelDiaryDAO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
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

    public List<CityDAO> getCityDAOList() {
        return cityDAOList;
    }

    public void setCityDAOList(List<CityDAO> cityDAOList) {
        this.cityDAOList = cityDAOList;
    }
}

package com.ghilly.model.DAO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cities")
public class CityDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_seq")
    @SequenceGenerator(name = "cities_seq", sequenceName = "cities_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "city")
    @NotBlank(message = "The city should have a name!")
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private CountryDAO countryDAO;


    @Column(name = "capital", columnDefinition = "boolean default false")
    private boolean capital;

    @ManyToMany
    @JoinTable(name = "cities_travel_diary",
            joinColumns = @JoinColumn(name = "city_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "travel_diary_id", referencedColumnName = "id"))
    private List<TravelDiaryDAO> listOfTravels;

    public CityDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public CityDAO(int id, String name, CountryDAO countryDAO, boolean capital) {
        this.id = id;
        this.name = name;
        this.countryDAO = countryDAO;
        this.capital = capital;
    }

    public CityDAO(int id, String name, boolean capital) {
        this.id = id;
        this.name = name;
        this.capital = capital;
    }

    public CityDAO(String name, boolean capital) {
        this.name = name;
        this.capital = capital;
    }

    public CityDAO(String name, CountryDAO countryDAO, boolean capital) {
        this.name = name;
        this.countryDAO = countryDAO;
        this.capital = capital;
    }

    public CityDAO(String name, CountryDAO countryDAO) {
        this.name = name;
        this.countryDAO = countryDAO;
    }

    public CityDAO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryDAO getCountry() {
        return countryDAO;
    }

    public void setCountry(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countryDAO);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityDAO cityDAO = (CityDAO) obj;
        return id == cityDAO.id && name.equals(cityDAO.name) && countryDAO == cityDAO.countryDAO && capital == cityDAO.capital;
    }

    @Override
    public String toString() {
        return "City" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country=" + countryDAO +
                ", capital=" + capital +
                '}';
    }
}

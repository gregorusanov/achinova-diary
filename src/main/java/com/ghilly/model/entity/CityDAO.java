package com.ghilly.model.entity;

import com.ghilly.model.Country;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
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
    private Country country;


    @Column(name = "capital", columnDefinition = "boolean default false")
    private boolean capital;

    public CityDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityDAO(int id, String name, Country country, boolean capital) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.capital = capital;
    }

    public CityDAO(String name, Country country, boolean capital) {
        this.name = name;
        this.country = country;
        this.capital = capital;
    }

    public CityDAO(String name, Country country) {
        this.name = name;
        this.country = country;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityDAO cityDAO = (CityDAO) obj;
        return id == cityDAO.id && name.equals(cityDAO.name) && country == cityDAO.country;
    }

    @Override
    public String toString() {
        return "City" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country=" + country +
                ", capital=" + capital +
                '}';
    }
}

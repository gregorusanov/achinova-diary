package com.ghilly.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="cities")
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_seq")
    @SequenceGenerator(name = "cities_seq", sequenceName = "cities_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;
    @Column(name="city")
    @NotBlank(message = "The city should have a name!")
    private String name;
    @Column(name="country_id")
    @NotBlank
    private int countryId;

    public City (int id, String name, int countryId){
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    public City (String name, int countryId){
        this.name = name;
        this.countryId = countryId;
    }

    public City() {

    }

    public City (String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCountryId() {
        return countryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countryId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City city = (City) obj;
        return id == city.id && name.equals(city.name) && countryId == city.countryId;
    }

    @Override
    public String toString() {
        return "City{" + "id = " + id + ", name = " + name + "country ID = " + countryId + '\'' + '}';
    }
}

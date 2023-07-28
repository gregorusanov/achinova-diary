package com.ghilly.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_id_seq", allocationSize = 1)
    @Column(name = "country_id")
    private int country_id;

    @Column(name = "country")
    @NotBlank(message = "The country should have a name!")
    private String name;

    public Country(int country_id, String name) {
        this.country_id = country_id;
        this.name = name;
    }

    public Country(String name) {
        this.name = name;
    }

    public Country() {

    }

    public int getCountry_id() {
        return country_id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return country_id == country.country_id && name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country_id, name);
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + country_id +
                ", name='" + name + '\'' +
                '}';
    }
}

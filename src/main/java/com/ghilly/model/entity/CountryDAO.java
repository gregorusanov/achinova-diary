package com.ghilly.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class CountryDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_id_seq", allocationSize = 1)
    @Column(name = "country_id")
    private int id;

    @Column(name = "country")
    @NotBlank(message = "The country should have a name!")
    private String name;

    public CountryDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CountryDAO(String name) {
        this.name = name;
    }

    public CountryDAO() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryDAO countryDAO = (CountryDAO) o;
        return id == countryDAO.id && name.equals(countryDAO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

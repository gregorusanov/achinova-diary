package com.ghilly.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class Countries implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "country")
    @NotBlank(message = "The country should have a name!")
    private String name;

    public Countries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Countries(String name) {
        this.name = name;
    }

    public Countries() {

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
        Countries countries = (Countries) o;
        return id == countries.id && name.equals(countries.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Countries{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}

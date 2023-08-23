package com.ghilly.model.DAO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class CountryDAO implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_id_seq", allocationSize = 1)
    @Column(name = "country_id")
    private int id;

    @Column(name = "country")
    @NotBlank(message = "The country should have a name!")
    private String name;

    public void setCityList(List<CityDAO> cityList) {
        this.cityList = cityList;
    }

    @OneToMany(mappedBy = "countryDAO")
    private List<CityDAO> cityList;


    public CountryDAO(int id, String name, List<CityDAO> cityList) {
        this.id = id;
        this.name = name;
        this.cityList = cityList;
    }

    public CountryDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CountryDAO(String name) {
        this.name = name;
    }

    public CountryDAO() {

    }

    public List<CityDAO> getCityList() {
        return cityList;
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
        return Objects.hash(id, name, cityList);
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public CountryDAO clone() {
        try {
            return (CountryDAO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

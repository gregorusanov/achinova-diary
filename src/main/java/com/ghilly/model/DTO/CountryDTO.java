package com.ghilly.model.DTO;

import java.util.List;
import java.util.Objects;

public class CountryDTO {
    private int id;
    private String name;
    //TODO remove this list
    private List<String> cityList;

    public CountryDTO(int id, String name, List<String> cityList) {
        this.id = id;
        this.name = name;
        this.cityList = cityList;
    }

    public CountryDTO(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cityList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CountryDTO countryDTO = (CountryDTO) obj;
        return id == countryDTO.id && name.equals(countryDTO.name);
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cities=" + cityList +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

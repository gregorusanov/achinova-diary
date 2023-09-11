package com.ghilly.model.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public class CountryDTO {
    private int id;
    private String name;

    public CountryDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CountryDTO() {
    }

    public CountryDTO(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
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
                ", name='" + name +
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

package com.ghilly.model.DTO;

import java.util.Objects;

public class CityDTO {
    private int id;
    private String name;
    private int countryId;
    private boolean capital;

    public CityDTO() {
    }

    public CityDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityDTO(int id, String name, int countryId, boolean capital) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.capital = capital;
    }

    public CityDTO(String name, int countryId, boolean capital) {
        this.name = name;
        this.countryId = countryId;
        this.capital = capital;
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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countryId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityDTO cityDTO = (CityDTO) obj;
        return id == cityDTO.id && name.equals(cityDTO.name) && countryId == cityDTO.countryId;
    }

    @Override
    public String toString() {
        return "City" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countryId=" + countryId +
                ", capital=" + capital +
                '}';
    }
}

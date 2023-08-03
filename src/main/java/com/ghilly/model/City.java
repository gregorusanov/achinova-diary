package com.ghilly.model;

import java.util.Objects;

public class City {

    private int cityId;
    private String name;
    private int countryId;
    private boolean capital;

    public City(int cityId, String name, int countryId, boolean capital) {
        this.cityId = cityId;
        this.name = name;
        this.countryId = countryId;
        this.capital = capital;
    }

    public City(int cityId, String name, int countryId) {
        this.cityId = cityId;
        this.name = name;
        this.countryId = countryId;
    }

    public City(int cityId, String name, boolean capital) {
        this.cityId = cityId;
        this.name = name;
        this.capital = capital;
    }

    public City(int cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public City(String name) {
        this.name = name;
    }

    public City(String name, int countryId, boolean capital) {
        this.name = name;
        this.countryId = countryId;
        this.capital = capital;
    }

    public City(String name, int countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public City(String name, boolean capital) {
        this.name = name;
        this.capital = capital;
    }

    public City() {

    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, name, countryId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City city = (City) obj;
        return cityId == city.cityId && name.equals(city.name) && countryId == city.countryId;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", name='" + name + '\'' +
                ", countryId=" + countryId +
                ", capital=" + capital +
                '}';
    }
}

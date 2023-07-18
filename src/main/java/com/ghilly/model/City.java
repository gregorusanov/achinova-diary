package com.ghilly.model;

import java.util.Objects;

public class City {
    private int id;
    private String name;
    Country country;

    public City (int id, String name, Country country){
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public City (String name, Country country){
        this.name = name;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City city = (City) obj;
        return id == city.id && name.equals(city.name) && country == city.country;
    }

    @Override
    public String toString() {
        return "City{" + "id = " + id + ", name = " + name + "country = " + country.getName() + '\'' + '}';
    }
}

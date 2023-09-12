package com.ghilly.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class City {
    private int id;
    private String name;
    private int countryId;
    private boolean capital;

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
}

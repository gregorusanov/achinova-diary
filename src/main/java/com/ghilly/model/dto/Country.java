package com.ghilly.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Country {
    private int id;
    private String name;

    public Country(String name) {
        this.name = name;
    }
}

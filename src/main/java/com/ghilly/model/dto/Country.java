package com.ghilly.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
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

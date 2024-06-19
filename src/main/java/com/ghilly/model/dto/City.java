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

}

package com.ghilly.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityTravelDiaryCompositeKey implements Serializable {

    @Column(name = "city_id")
    private int cityId;

    @Column(name = "travel_diary_id")
    private int travelDiaryId;
}

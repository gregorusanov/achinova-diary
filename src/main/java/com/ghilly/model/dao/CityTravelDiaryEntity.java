package com.ghilly.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "cities_travel_diary")
public class CityTravelDiaryEntity implements Serializable {

    @EmbeddedId
    private CityTravelDiaryCompositeKey id = new CityTravelDiaryCompositeKey();

    @ManyToOne
    @MapsId("cityId")
    @JoinColumn(name = "city_id")
    private CityEntity cityEntity;

    @ManyToOne
    @MapsId("travelDiaryId")
    @JoinColumn(name = "travel_diary_id")
    private TravelDiaryEntity travelDiaryEntity;
}

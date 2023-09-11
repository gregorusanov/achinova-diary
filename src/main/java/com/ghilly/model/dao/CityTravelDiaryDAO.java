package com.ghilly.model.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Table(name = "cities_travel_diary")
public class CityTravelDiaryDAO implements Serializable {

    @EmbeddedId
    private CityTravelDiaryCompositeKey id = new CityTravelDiaryCompositeKey();

    @ManyToOne
    @MapsId("cityId")
    @JoinColumn(name = "city_id")
    private CityDAO cityDAO;

    @ManyToOne
    @MapsId("travelDiaryId")
    @JoinColumn(name = "travel_diary_id")
    private TravelDiaryDAO travelDiaryDAO;
}

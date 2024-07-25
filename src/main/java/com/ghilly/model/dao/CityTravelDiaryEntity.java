package com.ghilly.model.dao;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "cities_travel_diary")
@Builder
public class CityTravelDiaryEntity implements Serializable {

    @EmbeddedId
    private CityTravelDiaryCompositeKey id = new CityTravelDiaryCompositeKey();

    @ManyToOne
    @MapsId("cityId")
    @JoinColumn(name = "city_id")
    @NonNull
    private CityEntity cityEntity;

    @ManyToOne
    @MapsId("travelDiaryId")
    @JoinColumn(name = "travel_diary_id")
    private TravelDiaryEntity travelDiaryEntity;
}

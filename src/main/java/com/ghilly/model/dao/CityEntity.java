package com.ghilly.model.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cities")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CityEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_seq")
    @SequenceGenerator(name = "cities_seq", sequenceName = "cities_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "city")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity countryEntity;

    @Column(name = "capital", columnDefinition = "boolean default false")
    private boolean capital;

    @OneToMany(mappedBy = "cityEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CityTravelDiaryEntity> travelDiaryCitySet = new HashSet<>();

    public CityEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityEntity(int id, String name, boolean capital) {
        this.id = id;
        this.name = name;
        this.capital = capital;
    }

    public CityEntity(int id, String name, CountryEntity countryEntity, boolean capital) {
        this.id = id;
        this.name = name;
        this.countryEntity = countryEntity;
        this.capital = capital;
    }

    public CityEntity(String name, boolean capital) {
        this.name = name;
        this.capital = capital;
    }

    public CityEntity(String name, CountryEntity countryEntity, boolean capital) {
        this.name = name;
        this.countryEntity = countryEntity;
        this.capital = capital;
    }

    public CityEntity(String name, CountryEntity countryEntity) {
        this.name = name;
        this.countryEntity = countryEntity;
    }
}

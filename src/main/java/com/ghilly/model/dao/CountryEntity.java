package com.ghilly.model.dao;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "countries")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CountryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_id_seq", allocationSize = 1)
    @Column(name = "country_id")
    private int id;

    @Column(name = "country")
    private String name;
    @OneToMany(mappedBy = "countryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CityEntity> citySet;


    public CountryEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CountryEntity(String name) {
        this.name = name;
    }
}

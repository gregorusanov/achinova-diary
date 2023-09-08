package com.ghilly.model.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "countries")
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CountryDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_id_seq", allocationSize = 1)
    @Column(name = "country_id")
    private int id;

    @Column(name = "country")
    private String name;
    @OneToMany(mappedBy = "countryDAO", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CityDAO> citySet;


    public CountryDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CountryDAO(String name) {
        this.name = name;
    }
}

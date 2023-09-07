package com.ghilly.model.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "countries")
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_seq")
    @SequenceGenerator(name = "countries_seq", sequenceName = "countries_id_seq", allocationSize = 1)
    @Column(name = "country_id")
    private int id;

    @Column(name = "country")
    private String name;
    @OneToMany(mappedBy = "countryDAO")
    private List<CityDAO> cityList;

    public CountryDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CountryDAO(String name) {
        this.name = name;
    }

    public CountryDAO(String name, List<CityDAO> cityList) {
        this.name = name;
        this.cityList = cityList;
    }
}

package com.ghilly.model.DAO;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cities")
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CityDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_seq")
    @SequenceGenerator(name = "cities_seq", sequenceName = "cities_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "city")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "country_id", nullable = false)
    private CountryDAO countryDAO;


    @Column(name = "capital", columnDefinition = "boolean default false")
    private boolean capital;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "cities_travel_diary",
            joinColumns = @JoinColumn(name = "city_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "travel_diary_id", referencedColumnName = "id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<TravelDiaryDAO> travelDiaryDAOSet = new HashSet<>();

    public CityDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityDAO(int id, String name, boolean capital) {
        this.id = id;
        this.name = name;
        this.capital = capital;
    }

    public CityDAO(int id, String name, CountryDAO countryDAO, boolean capital) {
        this.id = id;
        this.name = name;
        this.countryDAO = countryDAO;
        this.capital = capital;
    }

    public CityDAO(String name, boolean capital) {
        this.name = name;
        this.capital = capital;
    }

    public CityDAO(String name, CountryDAO countryDAO, boolean capital) {
        this.name = name;
        this.countryDAO = countryDAO;
        this.capital = capital;
    }

    public CityDAO(String name, CountryDAO countryDAO) {
        this.name = name;
        this.countryDAO = countryDAO;
    }
}

package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.model.dao.*;
import com.ghilly.model.dto.TravelDiary;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import com.ghilly.repository.TravelDiaryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class TravelDiaryIntegrationTest {
    private final String url = "/travelDiary/";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TravelDiaryRepository travelDiaryRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    @Transactional
    public void getTravelDiaryWithMultipleCities() throws Exception {
        CountryEntity ger = new CountryEntity("Germany");
        countryRepository.save(ger);
        String berlin = "Berlin";
        CityEntity firstCity = new CityEntity(berlin, ger, true);
        cityRepository.save(firstCity);
        int berlinId = cityRepository.findByName(berlin).orElseThrow().getId();
        CityEntity secondCity = new CityEntity("Munich", ger);
        cityRepository.save(secondCity);
        int munichId = cityRepository.findByName("Munich").orElseThrow().getId();
        TravelDiaryEntity travelDiary = TravelDiaryEntity.builder()
                .id(1)
                .arrivalDate(dateTransformer("09.03.2022"))
                .departureDate(dateTransformer("19.03.2022"))
                .plannedBudget(500)
                .realBudget(600)
                .description("Travel")
                .rating(5)
                .build();
        CityTravelDiaryEntity firstEntity = CityTravelDiaryEntity.builder()
                .id(new CityTravelDiaryCompositeKey())
                .travelDiaryEntity(travelDiary)
                .cityEntity(firstCity)
                .build();
        CityTravelDiaryEntity secondEntity = CityTravelDiaryEntity.builder()
                .id(new CityTravelDiaryCompositeKey())
                .travelDiaryEntity(travelDiary)
                .cityEntity(secondCity)
                .build();
        Set<CityTravelDiaryEntity> cityTravelDiarySet = new HashSet<>(Set.of(firstEntity, secondEntity));

        travelDiary.setCityTravelSet(cityTravelDiarySet);
        travelDiaryRepository.save(travelDiary);

        mvc.perform(MockMvcRequestBuilders
                        .get(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));

        Set<Integer> cities = new HashSet<>();
        travelDiaryRepository.findById(1).orElseThrow()
                .getCityTravelSet()
                .forEach(cityTravelDiaryEntity -> cities.add(cityTravelDiaryEntity.getCityEntity().getId()));

        assertEquals(cities, Set.of(berlinId, munichId));

        travelDiaryRepository.deleteAll();
        countryRepository.deleteAll();
    }

    private LocalDate dateTransformer(String date) {
        String pattern = "dd.MM.yyyy";
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}

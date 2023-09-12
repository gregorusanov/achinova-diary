package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CityTravelDiaryEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dao.TravelDiaryEntity;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void createAndGetStatusOk() throws Exception {
        CountryEntity ger = new CountryEntity("Germany");
        countryRepository.save(ger);
        String berlin = "Berlin";
        CityEntity cityEntity = new CityEntity(berlin, ger, true);
        cityRepository.save(cityEntity);
        int cityId = cityRepository.findByName(berlin).orElseThrow().getId();
        TravelDiary travelDiary = new TravelDiary(1, "09.03.2022", "10.03.2022",
                800, 1000, "Home.", 8, cityId);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(travelDiary);

        mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        Set<CityTravelDiaryEntity> travelDiaryEntity = travelDiaryRepository
                .findById(1)
                .map(TravelDiaryEntity::getCityTravelSet)
                .orElseThrow();

        assertEquals(travelDiaryEntity.size(), 1);

        travelDiaryRepository.deleteAll();
        countryRepository.deleteAll();
    }
}

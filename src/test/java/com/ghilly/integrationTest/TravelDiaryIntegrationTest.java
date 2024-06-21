package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.model.dao.*;
import com.ghilly.model.dto.City;
import com.ghilly.model.dto.Country;
import com.ghilly.model.dto.TravelDiary;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import com.ghilly.repository.TravelDiaryRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TravelDiaryIntegrationTest {
    private final String urlTD = "/travelDiary/";
    private final String urlCountry = "/countries/";
    private final String urlCity = "/cities/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TravelDiaryRepository travelDiaryRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void init() {
        travelDiaryRepository.deleteAll();
        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    @Transactional
    public void createAndGetTravelDiaryStatusOk() throws Exception {
        String countryName = "Netherlands";
        Country netherlands = new Country(countryName);
        String jsonCountry = objectMapper.writeValueAsString(netherlands);
        mvc.perform(MockMvcRequestBuilders
                        .post(urlCountry)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCountry));

        String hagueName = "Hague";
        City hague = City.builder().name(hagueName)
                .countryId(countryRepository.findByName(countryName.toLowerCase()).orElseThrow().getId()).build();
        String jsonCity = objectMapper.writeValueAsString(hague);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCity));

        int cityId = cityRepository.findByName(hagueName.toLowerCase()).orElseThrow().getId();
        TravelDiary travelDiary = TravelDiary.builder()
                .id(1)
                .arrivalDate("05.05.2023")
                .departureDate("12.05.2023")
                .rating(10)
                .cityIdSet(Set.of(cityId))
                .plannedBudget(1000)
                .realBudget(2000)
                .description("Wow")
                .build();
        String jsonTravelDiary = objectMapper.writeValueAsString(travelDiary);

        mvc.perform(MockMvcRequestBuilders
                .post(urlTD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTravelDiary))
                .andExpect(status().isOk());

        int id = travelDiaryRepository.findTravelDiaryEntityByDescription("Wow").getId();

        mvc.perform(MockMvcRequestBuilders
                .get(urlTD + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Wow"))
                .andExpect(jsonPath("$.rating").value(10));
    }

    @Test
    @Transactional
    public void getTravelDiaryWithMultipleCitiesStatusOk() throws Exception {
        String germanyName = "Germany";
        Country germany = new Country(germanyName);
        String jsonCountry = objectMapper.writeValueAsString(germany);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCountry)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCountry));
        int countryId = countryRepository.findByName(germanyName.toLowerCase()).orElseThrow().getId();

        String berlinName = "Berlin";
        City berlin = City.builder().name(berlinName)
                .countryId(countryId).build();
        String jsonBerlin = objectMapper.writeValueAsString(berlin);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBerlin));
        int berlinId = cityRepository.findByName(berlinName.toLowerCase()).orElseThrow().getId();

        String munichName = "Munich";
        City munich = City.builder().name(munichName)
                .countryId(countryId).build();
        String jsonMunich = objectMapper.writeValueAsString(munich);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMunich));
        int munichId = cityRepository.findByName(munichName.toLowerCase()).orElseThrow().getId();

        TravelDiary travelDiary = TravelDiary.builder()
                .arrivalDate("15.12.2023")
                .departureDate("23.12.2023")
                .plannedBudget(1000)
                .realBudget(1600)
                .description("Travel")
                .cityIdSet(Set.of(berlinId, munichId))
                .rating(5)
                .build();
        String jsonTravelDiary = objectMapper.writeValueAsString(travelDiary);
        mvc.perform(MockMvcRequestBuilders
                .post(urlTD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTravelDiary));


        int id = travelDiaryRepository.findTravelDiaryEntityByDescription("Travel").getId();
        // *TODO* create method in TDRepository which will return the unique TD record.

        mvc.perform(MockMvcRequestBuilders
                        .get(urlTD + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.plannedBudget").value(1000))
                .andExpect(jsonPath("$.rating").value(5));

        Set<Integer> cities = new HashSet<>();
        travelDiaryRepository.findById(id).orElseThrow()
                .getCityTravelSet()
                .forEach(cityTravelDiaryEntity -> cities.add(cityTravelDiaryEntity.getCityEntity().getId()));

        assertEquals(cities, Set.of(berlinId, munichId));
    }

    @Test
    @Transactional
    public void getAllStatusOk() throws Exception {
        String hollandName = "Netherlands";
        CountryEntity netherlands = new CountryEntity(hollandName);
        String jsonCountry = objectMapper.writeValueAsString(netherlands);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCountry)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCountry));
        int countryId = countryRepository.findByName(hollandName.toLowerCase()).orElseThrow().getId();

        String amsterdamName = "Amsterdam";
        City amsterdam = City.builder().name(amsterdamName).countryId(countryId).capital(true).build();
        String jsonAmsterdam = objectMapper.writeValueAsString(amsterdam);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAmsterdam));
        int amsterdamId = cityRepository.findByName(amsterdamName.toLowerCase()).orElseThrow().getId();
        TravelDiary travelToAmsterdam = TravelDiary.builder()
                .arrivalDate("20.05.2023")
                .departureDate("23.05.2023")
                .plannedBudget(1000)
                .realBudget(2000)
                .description("Freedom")
                .rating(10)
                .cityIdSet(Set.of(amsterdamId))
                .build();
        String jsonTDAmsterdam = objectMapper.writeValueAsString(travelToAmsterdam);
        mvc.perform(MockMvcRequestBuilders
                .post(urlTD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTDAmsterdam));

        String goudaName = "Gouda";
        City gouda = City.builder().name(goudaName).countryId(countryId).build();
        String jsonGouda = objectMapper.writeValueAsString(gouda);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonGouda));
        int goudaId = cityRepository.findByName(goudaName.toLowerCase()).orElseThrow().getId();
        TravelDiary travelToGouda = TravelDiary.builder()
                .arrivalDate("24.05.2023")
                .departureDate("25.05.2023")
                .plannedBudget(500)
                .realBudget(1000)
                .description("Gouda")
                .rating(8)
                .cityIdSet(Set.of(goudaId))
                .build();
        String jsonTDGouda = objectMapper.writeValueAsString(travelToGouda);
        mvc.perform(MockMvcRequestBuilders
                .post(urlTD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTDGouda));


        mvc.perform(MockMvcRequestBuilders
                    .get(urlTD + "all")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(Assertions::assertNotNull);

    }

    @Test
    @Transactional
    public void deleteStatusOk() throws Exception {
        String germanyName = "Germany";
        Country germany = new Country(germanyName);
        String jsonCountry = objectMapper.writeValueAsString(germany);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCountry)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCountry));

        String berlinName = "Berlin";
        City berlin = City.builder().name(berlinName)
                .countryId(countryRepository.findByName(germanyName.toLowerCase()).orElseThrow().getId()).build();
        String jsonCity = objectMapper.writeValueAsString(berlin);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCity));

        TravelDiary travelDiary = TravelDiary.builder()
                .arrivalDate("09.03.2022")
                .departureDate("19.03.2022")
                .plannedBudget(500)
                .realBudget(600)
                .description("Travel")
                .cityIdSet(Set.of(cityRepository.findByName(berlinName.toLowerCase()).orElseThrow().getId()))
                .rating(5)
                .build();
        String jsonTravelDiary = objectMapper.writeValueAsString(travelDiary);
        mvc.perform(MockMvcRequestBuilders
                        .post(urlTD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTravelDiary));

        int id = travelDiaryRepository.findTravelDiaryEntityByDescription("Travel").getId();

        mvc.perform(MockMvcRequestBuilders
                        .delete(urlTD + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(travelDiaryRepository.existsById(id));
    }
}

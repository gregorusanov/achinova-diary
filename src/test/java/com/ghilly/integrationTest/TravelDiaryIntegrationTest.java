package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final Country country = new Country();
    private final City city = City.builder().build();
    private final TravelDiary travelDiary = TravelDiary.builder()
            .arrivalDate("01.01.2020")
            .departureDate("01.01.2020")
            .plannedBudget(1)
            .realBudget(1)
            .description("")
            .rating(1)
            .build();
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
        country.setName(countryName);
        String jsonCountry = objectMapper.writeValueAsString(country);
        mvc.perform(MockMvcRequestBuilders
                        .post(urlCountry)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCountry));

        String hagueName = "Hague";
        city.setName(hagueName);
        city.setCountryId(countryRepository.findByName(countryName.toLowerCase()).orElseThrow().getId());
        String jsonCity = objectMapper.writeValueAsString(city);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCity));

        int cityId = cityRepository.findByName(hagueName.toLowerCase()).orElseThrow().getId();
        travelDiary.setArrivalDate("05.05.2023");
        travelDiary.setDepartureDate("12.05.2023");
        travelDiary.setRating(10);
        travelDiary.setCityIdSet(Set.of(cityId));
        travelDiary.setPlannedBudget(1000);
        travelDiary.setRealBudget(2000);
        travelDiary.setDescription("Wow");
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
        country.setName(germanyName);
        String jsonCountry = objectMapper.writeValueAsString(country);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCountry)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCountry));
        int countryId = countryRepository.findByName(germanyName.toLowerCase()).orElseThrow().getId();

        String berlinName = "Berlin";
        city.setName(berlinName);
        city.setCountryId(countryId);
        city.setCapital(true);
        String jsonBerlin = objectMapper.writeValueAsString(city);
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

        travelDiary.setArrivalDate("15.12.2023");
        travelDiary.setDepartureDate("23.12.2023");
        travelDiary.setRating(5);
        travelDiary.setPlannedBudget(1000);
        travelDiary.setRealBudget(1600);
        travelDiary.setDescription("Travel");
        travelDiary.setCityIdSet(Set.of(berlinId, munichId));
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
        country.setName(hollandName);
        String jsonCountry = objectMapper.writeValueAsString(country);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCountry)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCountry));
        int countryId = countryRepository.findByName(hollandName.toLowerCase()).orElseThrow().getId();

        String amsterdamName = "Amsterdam";
        city.setName(amsterdamName);
        city.setCountryId(countryId);
        city.setCapital(true);
        String jsonAmsterdam = objectMapper.writeValueAsString(city);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAmsterdam));
        int amsterdamId = cityRepository.findByName(amsterdamName.toLowerCase()).orElseThrow().getId();
        travelDiary.setArrivalDate("20.05.2023");
        travelDiary.setDepartureDate("23.05.2023");
        travelDiary.setRating(10);
        travelDiary.setPlannedBudget(1000);
        travelDiary.setRealBudget(2000);
        travelDiary.setDescription("Freedom");
        travelDiary.setCityIdSet(Set.of(amsterdamId));
        String jsonTDAmsterdam = objectMapper.writeValueAsString(city);
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
        country.setName(germanyName);
        String jsonCountry = objectMapper.writeValueAsString(country);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCountry)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCountry));
        int countryId = countryRepository.findByName(germanyName.toLowerCase()).orElseThrow().getId();

        String berlinName = "Berlin";
        city.setName(berlinName);
        city.setCountryId(countryId);
        String jsonCity = objectMapper.writeValueAsString(city);
        mvc.perform(MockMvcRequestBuilders
                .post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCity));
        int cityId = cityRepository.findByName(berlinName.toLowerCase()).orElseThrow().getId();

        travelDiary.setCityIdSet(Set.of(cityId));
        travelDiary.setDescription("delete");
        String jsonTravelDiary = objectMapper.writeValueAsString(travelDiary);
        mvc.perform(MockMvcRequestBuilders
                        .post(urlTD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTravelDiary));

        int id = travelDiaryRepository.findTravelDiaryEntityByDescription("delete").getId();

        mvc.perform(MockMvcRequestBuilders
                        .delete(urlTD + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(travelDiaryRepository.existsById(id));
    }

    @Test
    @Transactional
    public void updateSuccess() throws Exception {
        String countryName = "Russia";
        country.setName(countryName);
        String countryJson = objectMapper.writeValueAsString(country);
        mvc.perform(MockMvcRequestBuilders.post(urlCountry)
                .contentType(MediaType.APPLICATION_JSON)
                .content(countryJson))
                .andExpect(status().isOk());
        int countryId = countryRepository.findByName(countryName.toLowerCase()).orElseThrow().getId();

        String cityName = "Moscow";
        city.setName(cityName);
        city.setCountryId(countryId);
        city.setCapital(true);
        String cityJson = objectMapper.writeValueAsString(city);
        mvc.perform(MockMvcRequestBuilders.post(urlCity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cityJson))
                .andExpect(status().isOk());
        int cityId = cityRepository.findByName(cityName.toLowerCase()).orElseThrow().getId();

        travelDiary.setDescription("description");
        travelDiary.setCityIdSet(Set.of(cityId));
        String jsonTravelDiary = objectMapper.writeValueAsString(travelDiary);
        mvc.perform(MockMvcRequestBuilders
                .post(urlTD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTravelDiary))
                .andExpect(status().isOk());
        int id = travelDiaryRepository.findTravelDiaryEntityByDescription("description").getId();

        System.out.println(travelDiaryRepository.findById(id));

        travelDiary.setArrivalDate("03.04.2024");
        travelDiary.setDepartureDate("29.04.2024");
        travelDiary.setRating(10);
        travelDiary.setPlannedBudget(1000);
        travelDiary.setRealBudget(500);
        travelDiary.setDescription("Welcome home");
        String updated = objectMapper.writeValueAsString(travelDiary);

        mvc.perform(MockMvcRequestBuilders.put(urlTD + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(10))
                .andExpect(jsonPath("$.description").value("Welcome home"));
    }
}

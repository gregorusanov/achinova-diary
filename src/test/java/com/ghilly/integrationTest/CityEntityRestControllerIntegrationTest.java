package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.exception.CityAlreadyExistsException;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
import com.ghilly.model.dto.City;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
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

import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CityEntityRestControllerIntegrationTest {
    private static final String url = "/cities/";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Test
    public void createCityStatusOk() throws Exception {
        String jp = "japan";
        CountryEntity countryEntity = new CountryEntity(jp);
        countryRepository.save(countryEntity);
        int id = Objects.requireNonNull(countryRepository.findByName(jp).orElse(null)).getId();
        String tokyo = "Tokyo";
        City city = new City(tokyo, id, true);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(city);

        mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tokyo.toLowerCase()));

        countryRepository.deleteAll();
    }

    @Test
    public void createCityStatusConflict() throws Exception {
        String jp = "japan";
        CountryEntity japan = new CountryEntity(jp);
        countryRepository.save(japan);
        int id = Objects.requireNonNull(countryRepository.findByName(jp).orElse(null)).getId();
        String tokyo = "Tokyo";
        City city = new City(tokyo, id, true);
        cityRepository.save(new CityEntity(tokyo.toLowerCase(), japan, true));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(city);

        mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CityAlreadyExistsException))
                .andExpect(result -> assertEquals("The city " + tokyo.toLowerCase() + " already exists.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        countryRepository.deleteAll();
    }

    @Test
    public void getCityStatusOk() throws Exception {
        String fr = "France";
        String paris = "Paris";
        CountryEntity countryEntity = new CountryEntity(fr);
        countryRepository.save(countryEntity);
        CityEntity cityEntity = new CityEntity(paris, countryEntity, true);
        cityRepository.save(cityEntity);
        int cityId = cityRepository.findByName(paris).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .get(url + cityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(paris));
        assertTrue(cityRepository.findByName(paris).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void getCityStatusNotFound() throws Exception {
        String ger = "Germany";
        countryRepository.save(new CountryEntity(ger));

        mvc.perform(MockMvcRequestBuilders
                        .get(url + 30)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IdNotFoundException))
                .andExpect(result -> assertEquals("The city ID " + 30 + " is not found.",
                        Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));

        countryRepository.deleteAll();
    }

    @Test
    public void getAllCitiesStatusOk() throws Exception {
        String ber = "berlin";
        String mos = "moscow";
        String spb = "saint-Petersburg";
        String rus = "russia";
        String ger = "germany";
        CountryEntity germany = new CountryEntity(ger);
        countryRepository.save(germany);
        cityRepository.save(new CityEntity(ber, germany, true));
        CountryEntity russia = new CountryEntity(rus);
        countryRepository.save(russia);
        cityRepository.save(new CityEntity(mos, russia, true));
        cityRepository.save(new CityEntity(spb, russia));

        mvc.perform(MockMvcRequestBuilders
                        .get(url + "all/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(ber))
                .andExpect(jsonPath("$[1].name").value(mos))
                .andExpect(jsonPath("$[2].name").value(spb));

        countryRepository.deleteAll();
    }

    @Test
    public void updateStatusOk() throws Exception {
        String oldName = "Stalingrad";
        String newName = "Volgograd";
        CountryEntity rus = new CountryEntity("Russia");
        countryRepository.save(rus);
        int countryId = countryRepository.findByName(rus.getName()).orElseThrow().getId();
        cityRepository.save(new CityEntity(oldName, rus));
        int cityId = cityRepository.findByName(oldName).orElseThrow().getId();
        City volgograd = new City(newName, countryId);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(volgograd);

        mvc.perform(MockMvcRequestBuilders
                        .put(url + cityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(newName.toLowerCase()));

        countryRepository.deleteAll();
    }

    @Test
    public void deleteStatusOk() throws Exception {
        CountryEntity usa = new CountryEntity("USA");
        countryRepository.save(usa);
        String ny = "New York";
        cityRepository.save(new CityEntity(ny, usa, false));
        int cityId = cityRepository.findByName(ny).orElseThrow().getId();

        mvc.perform(MockMvcRequestBuilders
                        .delete(url + cityId))
                .andExpect(status().isOk());
        assertFalse(cityRepository.existsById(cityId));

        countryRepository.deleteAll();
    }

    @Test
    public void deleteStatusNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete(url + 300))
                .andExpect(status().isNotFound());
    }
}

package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.City;
import com.ghilly.model.Country;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
public class CityRestControllerIntegrationTest {
    private static String url = "/countries/cities/";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Test
    public void createCityStatusOk200() throws Exception {
        Country country = new Country("Japan");
        String tokyo = "Tokyo";
        countryRepository.save(country);
        City city = new City(tokyo, country, true);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(city);

        mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tokyo));
        assertTrue(cityRepository.findByName(tokyo).isPresent());

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void createCityStatusConflict409() throws Exception {
        String jp = "Japan";
        Country japan = new Country(jp);
        countryRepository.save(japan);
        String tokyo = "Tokyo";
        City city = new City(tokyo, japan, true);
        cityRepository.save(city);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(city);

        mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof
                        NameAlreadyExistsException))
                .andExpect(result -> assertEquals("The city with the name " + tokyo + " already exists.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void getCityStatusOk200() throws Exception {
        String fr = "France";
        String paris = "Paris";
        Country country = new Country(fr);
        countryRepository.save(country);
        City city = new City(paris, country, true);
        cityRepository.save(city);
        int cityId = cityRepository.findByName(paris).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .get(url + cityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(paris));
        assertTrue(cityRepository.findByName(paris).isPresent());

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void getCityStatusNotFound404() throws Exception {
        String ger = "Germany";
        countryRepository.save(new Country(ger));

        mvc.perform(MockMvcRequestBuilders
                        .get(url + 30)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IdNotFoundException))
                .andExpect(result -> assertEquals("The city with the ID " + 30 + " is not found.",
                        Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void getAllCitiesStatusOk200() throws Exception {
        String ber = "Berlin";
        String mos = "Moscow";
        String spb = "Saint-Petersburg";
        String rus = "Russia";
        String ger = "Germany";
        Country germany = new Country(ger);
        countryRepository.save(germany);
        cityRepository.save(new City(ber, germany, true));
        Country russia = new Country(rus);
        countryRepository.save(russia);
        cityRepository.save(new City(mos, russia, true));
        cityRepository.save(new City(spb, russia));

        mvc.perform(MockMvcRequestBuilders
                        .get(url + "all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(ber))
                .andExpect(jsonPath("$[1].name").value(mos))
                .andExpect(jsonPath("$[2].name").value(spb));
        assertTrue(cityRepository.findByName(ber).isPresent());
        assertTrue(cityRepository.findByName(mos).isPresent());
        assertTrue(cityRepository.findByName(spb).isPresent());

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void updateStatusOk200() throws Exception {
        String oldName = "Stalingrad";
        String newName = "Volgograd";
        Country rus = new Country("Russia");
        countryRepository.save(rus);
        cityRepository.save(new City(oldName, rus, false));
        int cityId = cityRepository.findByName(oldName).get().getId();
        City volgograd = new City(cityId, newName, rus, false);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(volgograd);

        mvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(newName));
        assertTrue(cityRepository.findByName(newName).isPresent());

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void deleteStatusOk200() throws Exception {
        Country usa = new Country("USA");
        countryRepository.save(usa);
        String ny = "New York";
        cityRepository.save(new City(ny, usa, false));
        int cityId = cityRepository.findByName(ny).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .delete(url + cityId))
                .andExpect(status().isOk());
        assertFalse(cityRepository.existsById(cityId));

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void deleteStatusNotFound404() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete(url + 300))
                .andExpect(status().isNotFound());
    }
}

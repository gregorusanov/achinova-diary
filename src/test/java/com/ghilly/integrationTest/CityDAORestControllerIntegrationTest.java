package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.City;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
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
public class CityDAORestControllerIntegrationTest {
    private static final String url = "/cities/";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Test
    public void createCityStatusOk200() throws Exception {
        String jp = "Japan";
        CountryDAO countryDAO = new CountryDAO(jp);
        String tokyo = "Tokyo";
        countryRepository.save(countryDAO);
        int id = countryRepository.findByName(jp).get().getId();
        City city = new City(tokyo, true);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(city);

        mvc.perform(MockMvcRequestBuilders
                        .post(url + id)
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
        CountryDAO japan = new CountryDAO(jp);
        countryRepository.save(japan);
        int id = countryRepository.findByName(jp).get().getId();
        String tokyo = "Tokyo";
        City city = new City(tokyo, true);
        cityRepository.save(new CityDAO(tokyo, japan, true));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(city);

        mvc.perform(MockMvcRequestBuilders
                        .post(url + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof
                        NameAlreadyExistsException))
                .andExpect(result -> assertEquals("The city name " + tokyo + " already exists.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void getCityStatusOk200() throws Exception {
        String fr = "France";
        String paris = "Paris";
        CountryDAO countryDAO = new CountryDAO(fr);
        countryRepository.save(countryDAO);
        CityDAO cityDAO = new CityDAO(paris, countryDAO, true);
        cityRepository.save(cityDAO);
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
        countryRepository.save(new CountryDAO(ger));

        mvc.perform(MockMvcRequestBuilders
                        .get(url + 30)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IdNotFoundException))
                .andExpect(result -> assertEquals("The city ID " + 30 + " is not found.",
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
        CountryDAO germany = new CountryDAO(ger);
        countryRepository.save(germany);
        cityRepository.save(new CityDAO(ber, germany, true));
        CountryDAO russia = new CountryDAO(rus);
        countryRepository.save(russia);
        cityRepository.save(new CityDAO(mos, russia, true));
        cityRepository.save(new CityDAO(spb, russia));

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
        CountryDAO rus = new CountryDAO("Russia");
        countryRepository.save(rus);
        cityRepository.save(new CityDAO(oldName, rus));
        int cityId = cityRepository.findByName(oldName).get().getId();
        City volgograd = new City(cityId, newName);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(volgograd);

        mvc.perform(MockMvcRequestBuilders
                        .put(url + cityId)
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
        CountryDAO usa = new CountryDAO("USA");
        countryRepository.save(usa);
        String ny = "New York";
        cityRepository.save(new CityDAO(ny, usa, false));
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

    @Test
    public void getCountryByCityIdStatusOk200() throws Exception {
        String country = "Belgium";
        String city = "Brussels";
        CountryDAO countryDAO = new CountryDAO(country);
        CityDAO cityDAO = new CityDAO(city, countryDAO, true);
        countryRepository.save(countryDAO);
        cityRepository.save(cityDAO);
        int cityId = cityRepository.findByName(city).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .get(url + "all/" + cityId + "/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(country));

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void getCountryByCityIdStatusNotFound404() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get(url + "all/" + 404 + "/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof IdNotFoundException))
                .andExpect(result -> assertEquals("The city ID " + 404 + " is not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }
}

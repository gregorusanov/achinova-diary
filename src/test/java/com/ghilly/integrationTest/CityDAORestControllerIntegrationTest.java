package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.exception.CityAlreadyExistsException;
import com.ghilly.exception.IdNotFoundException;
import com.ghilly.model.dao.CityDAO;
import com.ghilly.model.dao.CountryDAO;
import com.ghilly.model.dto.CityDTO;
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
public class CityDAORestControllerIntegrationTest {
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
        CountryDAO countryDAO = new CountryDAO(jp);
        countryRepository.save(countryDAO);
        int id = Objects.requireNonNull(countryRepository.findByName(jp).orElse(null)).getId();
        String tokyo = "Tokyo";
        CityDTO city = new CityDTO(tokyo, id, true);
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
        CountryDAO japan = new CountryDAO(jp);
        countryRepository.save(japan);
        int id = Objects.requireNonNull(countryRepository.findByName(jp).orElse(null)).getId();
        String tokyo = "Tokyo";
        CityDTO city = new CityDTO(tokyo, id, true);
        cityRepository.save(new CityDAO(tokyo.toLowerCase(), japan, true));
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

        countryRepository.deleteAll();
    }

    @Test
    public void getCityStatusNotFound() throws Exception {
        String ger = "Germany";
        countryRepository.save(new CountryDAO(ger));

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
        CountryDAO germany = new CountryDAO(ger);
        countryRepository.save(germany);
        cityRepository.save(new CityDAO(ber, germany, true));
        CountryDAO russia = new CountryDAO(rus);
        countryRepository.save(russia);
        cityRepository.save(new CityDAO(mos, russia, true));
        cityRepository.save(new CityDAO(spb, russia));

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
        CountryDAO rus = new CountryDAO("Russia");
        countryRepository.save(rus);
        int countryId = countryRepository.findByName(rus.getName()).orElseThrow().getId();
        cityRepository.save(new CityDAO(oldName, rus));
        int cityId = cityRepository.findByName(oldName).orElseThrow().getId();
        CityDTO volgograd = new CityDTO(newName, countryId);
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
        CountryDAO usa = new CountryDAO("USA");
        countryRepository.save(usa);
        String ny = "New York";
        cityRepository.save(new CityDAO(ny, usa, false));
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

package com.ghilly.integrationTest;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CityRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void createCityStatusOk200() throws Exception {
        String jp = "Japan";
        String tokyo = "Tokyo";
        countryRepository.save(new Country(jp));
        int id = countryRepository.findByName(jp).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .post("/countries/{countryId}/cities/", id)
                        .content(tokyo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tokyo));
        assertTrue(cityRepository.findByName(tokyo).isPresent());

        countryRepository.deleteAll();
        cityRepository.deleteAll();
    }

    @Test
    public void createCityStatusConflict409() throws Exception {
        String jp = "Japan";
        String tokyo = "Tokyo";
        countryRepository.save(new Country(jp));
        int id = countryRepository.findByName(jp).get().getId();
        cityRepository.save(new City(tokyo, id));

        mvc.perform(MockMvcRequestBuilders
                        .post("/countries/{countryId}/cities/", id)
                        .content(tokyo))
                .andExpect(status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NameAlreadyExistsException))
                .andExpect(result -> assertEquals("The city with the name " + tokyo + " already exists.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        countryRepository.deleteAll();
        cityRepository.deleteAll();
    }
}

package com.ghilly.integrationTest;

import com.ghilly.model.Country;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.CountryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertTrue;
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
    public void createCountryStatusOk200() throws Exception {
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
}

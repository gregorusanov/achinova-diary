package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.model.DTO.TravelDiaryDTO;
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
    public void createAndGetStatusOk() throws Exception {
        CountryDAO ger = new CountryDAO("Germany");
        countryRepository.save(ger);
        String berlin = "Berlin";
        CityDAO cityDAO = new CityDAO(berlin, ger, true);
        cityRepository.save(cityDAO);
        int cityId = cityRepository.findByName(berlin).orElseThrow().getId();
        TravelDiaryDTO travelDiaryDTO = new TravelDiaryDTO(1, "09.03.2022", "10.03.2022",
                800, 1000, "Home.", 8, cityId);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(travelDiaryDTO);

        mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        countryRepository.deleteAll();
    }
}

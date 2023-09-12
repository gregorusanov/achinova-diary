package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.exception.*;
import com.ghilly.model.dao.CityEntity;
import com.ghilly.model.dao.CountryEntity;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CatchExceptionIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void catchNameAlreadyExistsExceptionStatusBadRequest() throws Exception {
        String rus = "russia";
        CountryEntity countryEntity = new CountryEntity(rus);
        countryRepository.save(countryEntity);
        countryEntity.setName(rus.toUpperCase());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(countryEntity);

        mvc.perform(MockMvcRequestBuilders
                        .post("/countries/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NameAlreadyExistsException))
                .andExpect(result -> assertEquals("The country name " + rus + " already exists.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        countryRepository.deleteAll();
    }

    @Test
    public void catchIdIsNotFoundStatusNotFound() throws Exception {
        int id = 400;

        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/{countryId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IdNotFoundException))
                .andExpect(result -> assertEquals("The country ID " + id + " is not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        countryRepository.deleteAll();
    }

    @Test
    public void catchWrongArgumentNameExceptionStatusNotAcceptable() throws Exception {
        String wrongName = "Rus777";
        CountryEntity countryEntity = new CountryEntity(wrongName);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(countryEntity);

        mvc.perform(MockMvcRequestBuilders
                        .post("/countries/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof WrongNameException))
                .andExpect(result -> assertEquals("Warning! \n The legal name consists of letters " +
                        "that could be separated by one space or hyphen. \n The name is not allowed here: " +
                        wrongName.toLowerCase(), Objects.requireNonNull(result.getResolvedException()).getMessage()));
        countryRepository.deleteAll();
    }

    @Test
    public void catchCapitalAlreadyExistsStatusBadRequest() throws Exception {
        String rus = "Russia";
        countryRepository.save(new CountryEntity(rus));
        CountryEntity countryEntity = countryRepository.findByName(rus).orElseThrow();
        int countryId = countryEntity.getId();
        String moscow = "Moscow";
        cityRepository.save(new CityEntity(moscow, countryEntity, true));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new CityDTO("Saint-Petersburg", countryId, true));

        mvc.perform(MockMvcRequestBuilders
                        .post("/cities/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CapitalAlreadyExistsException))
                .andExpect(result -> assertEquals("The capital for the country ID " + countryId +
                                " is already set. Try to update this city.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void catchCityAlreadyExistsStatusBadRequest() throws Exception {
        String rus = "russia";
        countryRepository.save(new CountryEntity(rus));
        CountryEntity countryEntity = countryRepository.findByName(rus).orElseThrow();
        int countryId = countryEntity.getId();
        String moscow = "moscow";
        cityRepository.save(new CityEntity(moscow, countryEntity, true));
        CityEntity cityEntity = cityRepository.findByName(moscow).orElseThrow();
        int id = cityEntity.getId();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new CityDTO(id, moscow.toUpperCase(), countryId, true));

        mvc.perform(MockMvcRequestBuilders
                        .put("/cities/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CityAlreadyExistsException))
                .andExpect(result -> assertEquals("The city " + moscow + " already exists.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }
}

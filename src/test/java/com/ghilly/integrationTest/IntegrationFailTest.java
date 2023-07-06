package com.ghilly.integrationTest;

import com.ghilly.exception.EmptyNameException;
import com.ghilly.exception.IdIsNotFoundException;
import com.ghilly.exception.NameAlreadyExistsException;
import com.ghilly.model.Country;
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
public class IntegrationFailTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CountryRepository repository;

    @Test
    public void createCountryStatusConflict409() throws Exception {
        String rus = "Russia";
        repository.save(new Country(rus));

        mvc.perform(MockMvcRequestBuilders
                    .post("/countries/")
                    .content(rus))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NameAlreadyExistsException))
                .andExpect(result -> assertEquals("The country with this name " + rus + " already exists.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        repository.deleteAll();
    }

    @Test
    public void createCountryStatusNotAcceptable406() throws Exception {
        String toClear = "12";
        repository.save(new Country(toClear));

        mvc.perform(MockMvcRequestBuilders
                        .post("/countries/")
                        .content(toClear))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmptyNameException))
                .andExpect(result -> assertEquals("The country name should not be empty!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    public void getCountryStatusNotFound404() throws Exception {
        int id = 400;

        mvc.perform(MockMvcRequestBuilders
                    .get("/countries/{countryId}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IdIsNotFoundException))
                .andExpect(result -> assertEquals("The country with this ID " + id + " is not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    public void updateCountryStatusNotFound404() throws Exception {
        int id = 289;
        String name = "Mongolia";

        mvc.perform(MockMvcRequestBuilders
                        .put("/countries/{countryId}", id)
                        .content(name))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IdIsNotFoundException))
                .andExpect(result -> assertEquals("The country with this ID " + id + " is not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    public void deleteCountryStatusNotFound404() throws Exception {
        int id = 666;

        mvc.perform(MockMvcRequestBuilders
                        .delete("/countries/{countryId}", id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IdIsNotFoundException))
                .andExpect(result -> assertEquals("The country with this ID " + id + " is not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }
}

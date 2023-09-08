package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.model.DTO.CountryDTO;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CountryDAORestControllerIntegrationSuccessfulTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void createCountryStatusOk() throws Exception {
        String jp = "japan";
        CountryDTO japan = new CountryDTO(jp);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(japan);

        mvc.perform(MockMvcRequestBuilders
                        .post("/countries/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
        assertTrue(countryRepository.findByName(jp.toLowerCase()).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void getAllCountriesStatusOk() throws Exception {
        String rus = "russia";
        String fr = "france";
        String de = "deutschland";
        countryRepository.save(new CountryDAO(de));
        countryRepository.save(new CountryDAO(fr));
        countryRepository.save(new CountryDAO(rus));

        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(de))
                .andExpect(jsonPath("$[1].name").value(fr))
                .andExpect(jsonPath("$[2].name").value(rus));
        assertTrue(countryRepository.findByName(rus).isPresent());
        assertTrue(countryRepository.findByName(fr).isPresent());
        assertTrue(countryRepository.findByName(de).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void getCountryStatusOk() throws Exception {
        String cn = "China";
        countryRepository.save(new CountryDAO(cn));
        int id = countryRepository.findByName(cn).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(cn));
        assertTrue(countryRepository.findByName(cn).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void updateCountryStatusOk() throws Exception {
        String ussr = "ussr";
        CountryDAO countryDAO = new CountryDAO(ussr);
        countryRepository.save(countryDAO);
        int id = countryRepository.findByName(ussr).orElseThrow().getId();
        String newName = "Russia";
        CountryDTO toUpdate = new CountryDTO(id, newName);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);

        mvc.perform(MockMvcRequestBuilders
                        .put("/countries/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(System.out::println)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(newName.toLowerCase()));
        assertTrue(countryRepository.findByName(newName.toLowerCase()).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void deleteCountryStatusOk() throws Exception {
        String gr = "Greece";
        countryRepository.save(new CountryDAO(gr));
        int id = countryRepository.findByName(gr).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .delete("/countries/{countryId}", id))
                .andExpect(status().isOk());
        assertFalse(countryRepository.findByName(gr).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void getAllCitiesByCountryStatusOk() throws Exception {
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
        int id = countryRepository.findByName(rus).orElseThrow().getId();
        cityRepository.save(new CityDAO(mos, russia, true));
        cityRepository.save(new CityDAO(spb, russia));

        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/" + id + "/cities/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(mos))
                .andExpect(jsonPath("$[1].name").value(spb));

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void getCapitalByCountryIdStatusOk() throws Exception {
        String cn = "China";
        String bj = "Beijing";
        CountryDAO china = new CountryDAO(cn);
        countryRepository.save(china);
        int id = countryRepository.findByName(cn).orElseThrow().getId();
        cityRepository.save(new CityDAO(bj, china, true));

        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/" + id + "/capital")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(bj));

        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void getAllCountriesEmptySet() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").doesNotExist());
    }
}

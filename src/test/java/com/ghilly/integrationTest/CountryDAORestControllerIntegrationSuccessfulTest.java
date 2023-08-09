package com.ghilly.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghilly.model.entity.CityDAO;
import com.ghilly.model.entity.CountryDAO;
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
    public void createCountryStatusOk200() throws Exception {
        String jp = "Japan";
        CountryDAO japan = new CountryDAO(jp);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(japan);

        mvc.perform(MockMvcRequestBuilders
                        .post("/countries/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
        assertTrue(countryRepository.findByName(jp).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void getAllCountriesStatusOk200() throws Exception {
        String rus = "Russia";
        String fr = "France";
        String de = "Deutschland";
        countryRepository.save(new CountryDAO(rus));
        countryRepository.save(new CountryDAO(fr));
        countryRepository.save(new CountryDAO(de));

        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(System.out::println)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(rus))
                .andExpect(jsonPath("$[1].name").value(fr))
                .andExpect(jsonPath("$[2].name").value(de));
        assertTrue(countryRepository.findByName(rus).isPresent());
        assertTrue(countryRepository.findByName(fr).isPresent());
        assertTrue(countryRepository.findByName(de).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void getCountryStatusOk200() throws Exception {
        String cn = "China";
        countryRepository.save(new CountryDAO(cn));
        int id = countryRepository.findByName(cn).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .get("/countries/{countryId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(System.out::println)
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(cn));
        assertTrue(countryRepository.findByName(cn).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void updateCountryStatusOk200() throws Exception {
        CountryDAO countryDAO = new CountryDAO("USSR");
        countryRepository.save(countryDAO);
        int id = countryRepository.findByName("USSR").get().getId();
        String newName = "Russia";
        CountryDAO toUpdate = new CountryDAO(id, newName);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(toUpdate);

        mvc.perform(MockMvcRequestBuilders
                        .put("/countries/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(System.out::println)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(newName));
        assertTrue(countryRepository.findByName(newName).isPresent());

        countryRepository.deleteAll();
    }

    @Test
    public void deleteCountryStatusOk200() throws Exception {
        String gr = "Greece";
        countryRepository.save(new CountryDAO(gr));
        int id = countryRepository.findByName(gr).get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .delete("/countries/{countryId}", id))
                .andExpect(status().isOk());
        assertFalse(countryRepository.findByName(gr).isPresent());

        countryRepository.deleteAll();
    }

//    @Test
//    public void getCountryByCityIdStatusOk200() throws Exception {
//        String country = "Belgium";
//        String city = "Brussels";
//        CountryDAO countryDAO = new CountryDAO(country);
//        CityDAO cityDAO = new CityDAO(city, countryDAO, true);
//        countryRepository.save(countryDAO);
//        cityRepository.save(cityDAO);
//        int cityId = cityRepository.findByName(city).get().getId();
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get("/countries/all/city/{cityId}", cityId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value(country));
//
//        countryRepository.deleteAll();
//        cityRepository.deleteAll();
//    }
}

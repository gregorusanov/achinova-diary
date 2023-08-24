package com.ghilly.transformer;

import com.ghilly.model.DAO.CityDAO;
import com.ghilly.model.DAO.CountryDAO;
import com.ghilly.model.DTO.CityDTO;
import com.ghilly.model.DTO.CountryDTO;

import java.util.ArrayList;

public class TransformerDAODTO {
    private TransformerDAODTO() {
    }

    public static CountryDAO transformToCountryDAO(CountryDTO countryDTO) {
        return new CountryDAO(countryDTO.getId(), countryDTO.getName(), new ArrayList<>());
    }

    public static CountryDTO transformToCountryDTO(CountryDAO countryDAO) {
        int id = countryDAO.getId();
        String name = countryDAO.getName();
        return new CountryDTO(id, name);
    }

    public static CityDTO transformToCityDTO(CityDAO cityDAO) {
        return new CityDTO(cityDAO.getId(), cityDAO.getName(), cityDAO.getCountry().getId(), cityDAO.isCapital());
    }

    public static CityDAO transformToCityDAO(CityDTO cityDTO) {
        return new CityDAO(cityDTO.getId(), cityDTO.getName(), null, cityDTO.isCapital());
    }
}

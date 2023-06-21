package com.ghilly.service;

//import com.ghilly.classes.Country;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Service {
    void add(String countryName);

    List<String> receiveList();

    String receiveCountry(int countryId);

    void upgrade(int id, String newName);

    void clear(int id);
}

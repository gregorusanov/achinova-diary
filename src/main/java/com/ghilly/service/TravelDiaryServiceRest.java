package com.ghilly.service;

import com.ghilly.model.dao.TravelDiaryDAO;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.TravelDiaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TravelDiaryServiceRest implements TravelDiaryService {
    private static final Logger logger = LoggerFactory.getLogger(TravelDiaryServiceRest.class);
    private final CityRepository cityRepository;
    private final TravelDiaryRepository travelDiaryRepository;

    public TravelDiaryServiceRest(CityRepository cityRepository, TravelDiaryRepository travelDiaryRepository) {
        this.cityRepository = cityRepository;
        this.travelDiaryRepository = travelDiaryRepository;
    }

    @Override
    public TravelDiaryDAO create(TravelDiaryDAO travelDiaryDAO) {
        logger.info("Transferring data {} to the repository", travelDiaryDAO);
        return travelDiaryRepository.save(travelDiaryDAO);
    }

}

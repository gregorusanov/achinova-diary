package com.ghilly.service;

import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.TravelDiaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class TravelDiaryServiceRest implements TravelDiaryService {
    private static final Logger logger = LoggerFactory.getLogger(TravelDiaryServiceRest.class);
    private final CityRepository cityRepository;
    private final TravelDiaryRepository travelDiaryRepository;

    public TravelDiaryServiceRest(CityRepository cityRepository, TravelDiaryRepository travelDiaryRepository) {
        this.cityRepository = cityRepository;
        this.travelDiaryRepository = travelDiaryRepository;
    }

    @Override
    public TravelDiaryEntity create(TravelDiaryEntity travelDiaryEntity) {
        logger.info("Transferring data {} to the repository", travelDiaryEntity);
        return travelDiaryRepository.save(travelDiaryEntity);
    }

    @Override
    public Optional<TravelDiaryEntity> getTravelDiaryEntityById(int id) {
        Optional<TravelDiaryEntity> travelDiaryEntity = travelDiaryRepository.findById(id);
        logger.info("Got record from diary by id [{}], result is [{}]", id, travelDiaryEntity);
        return travelDiaryEntity;
    }

    public boolean travelIdExists(int id) {
        return travelDiaryRepository.findById(id).isPresent();
    }

}

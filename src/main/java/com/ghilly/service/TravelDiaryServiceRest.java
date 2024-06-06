package com.ghilly.service;

import com.ghilly.exception.IdNotFoundException;
import com.ghilly.model.dao.TravelDiaryEntity;
import com.ghilly.repository.CityRepository;
import com.ghilly.repository.TravelDiaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;


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

    @Override
    public Set<TravelDiaryEntity> getAll() {
        logger.info("Getting data from the repository.");
        return (Set<TravelDiaryEntity>) travelDiaryRepository.findAll();
    }

    @Override
    public void delete(int id) {
        travelDiaryRepository.deleteById(id);
        logger.info("Getting data from the repository.");
    }

    public boolean travelIdExists(int id) {
        return travelDiaryRepository.findById(id).isPresent();
    }

}

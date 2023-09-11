package com.ghilly.web.controller;

import com.ghilly.model.DTO.TravelDiaryDTO;
import com.ghilly.transformer.TransformerDAOandDTO;
import com.ghilly.web.handler.TravelDiaryHandler;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/travelDiary")
public class TravelDiaryController {
    //CityController.class???
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CityController.class);
    private final TravelDiaryHandler handler;

    public TravelDiaryController(TravelDiaryHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/")
    public ResponseEntity<TravelDiaryDTO> create(@RequestBody TravelDiaryDTO travelDiary) {
        logger.info("Transfer data {} to handler.", travelDiary);
        return Optional.of(travelDiary)
                .map(TransformerDAOandDTO::transformToTravelDiaryDAO)
                .map(travelDiaryDAO -> handler.create(travelDiaryDAO, travelDiary.getCityId()))
                .map(TransformerDAOandDTO::transformToTravelDiaryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}

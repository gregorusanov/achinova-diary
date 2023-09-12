package com.ghilly.web.controller;

import com.ghilly.model.dto.TravelDiary;
import com.ghilly.transformer.EntityTransformer;
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
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TravelDiaryController.class);
    private final TravelDiaryHandler handler;

    public TravelDiaryController(TravelDiaryHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/")
    public ResponseEntity<TravelDiary> create(@RequestBody TravelDiary travelDiary) {
        logger.info("Transfer data {} to handler.", travelDiary);
        return Optional.of(travelDiary)
                .map(EntityTransformer::transformToTravelDiaryEntity)
                .map(travelDiaryEntity -> handler.create(travelDiaryEntity, travelDiary.getCityId()))
                .map(EntityTransformer::transformToTravelDiary)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}

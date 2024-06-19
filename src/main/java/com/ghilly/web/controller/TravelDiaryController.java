package com.ghilly.web.controller;

import com.ghilly.model.dto.TravelDiary;
import com.ghilly.transformer.EntityTransformer;
import com.ghilly.web.handler.TravelDiaryHandler;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                .map(travelDiaryEntity -> handler.create(travelDiaryEntity, travelDiary.getCityIdSet()))
                .map(EntityTransformer::transformToTravelDiary)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<TravelDiary> getTravelById(@PathVariable int travelId) {
        logger.info("Transfer data {} to the handler.", travelId);
        return handler.getTravelDiaryEntityById(travelId)
                .map(EntityTransformer::transformToTravelDiary)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/all")
    public ResponseEntity<Set<TravelDiary>> getAll() {
        logger.info("Getting the data from the handler.");
        Set<TravelDiary> set = handler.getAll().stream()
                .map(EntityTransformer::transformToTravelDiary)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(set);
    }

    @DeleteMapping("/{travelId}")
    public ResponseEntity<String> delete(@PathVariable int travelId) {
        logger.info("The data are received from the user.");
        handler.delete(travelId);
        return ResponseEntity.ok().body("The record with the ID " + travelId + " is deleted.");
    }
}

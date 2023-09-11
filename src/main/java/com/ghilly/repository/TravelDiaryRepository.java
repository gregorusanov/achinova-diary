package com.ghilly.repository;

import com.ghilly.model.dao.TravelDiaryDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TravelDiaryRepository extends CrudRepository<TravelDiaryDAO, Integer> {
}
package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.Coordinate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CoordinateRepository extends MongoRepository<Coordinate, String> {

    long countByUserId(String userId);

    List<Coordinate> findByUserId(String userId);
}

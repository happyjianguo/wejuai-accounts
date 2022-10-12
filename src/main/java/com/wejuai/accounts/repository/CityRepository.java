package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author ZM.Wang
 */
public interface CityRepository extends MongoRepository<City, String> {

    List<City> findByPid(String pid);
}

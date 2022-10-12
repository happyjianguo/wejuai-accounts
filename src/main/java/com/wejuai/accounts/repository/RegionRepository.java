package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.Region;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author ZM.Wang
 */
public interface RegionRepository extends MongoRepository<Region, String> {

    List<Region> findByPid(String pid);
}

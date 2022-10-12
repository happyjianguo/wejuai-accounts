package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mongo.Star;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ZM.Wang
 */
public interface StarRepository extends MongoRepository<Star, String> {

    long countByCreateUserId(String createUserId);

    Star findByUserIdAndAppTypeAndAppId(String userId, AppType appType, String appId);
}

package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mongo.Collection;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ZM.Wang
 */
public interface CollectionRepository extends MongoRepository<Collection, String> {

    long countByCreateUserId(String createUserId);

    long countByUserId(String userId);

    Collection findByUserIdAndAppTypeAndAppId(String userId, AppType appType, String appId);
}

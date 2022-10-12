package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.SystemMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ZM.Wang
 */
public interface SystemMessageRepository extends MongoRepository<SystemMessage, String> {

    long countByUserIdAndWatchFalse(String userId);

    Page<SystemMessage> findByUserId(String userId, Pageable pageable);
}

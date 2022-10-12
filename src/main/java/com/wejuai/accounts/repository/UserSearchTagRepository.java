package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.UserSearchTag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserSearchTagRepository extends MongoRepository<UserSearchTag, String> {
}

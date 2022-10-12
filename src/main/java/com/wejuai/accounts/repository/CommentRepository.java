package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

    long countByAppCreatorAndWatchFalse(String appCreator);
}

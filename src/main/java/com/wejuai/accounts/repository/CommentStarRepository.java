package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.CommentStar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentStarRepository extends MongoRepository<CommentStar, String> {

    boolean existsByCommentIdAndUserId(String id, String userId);

    CommentStar findByCommentIdAndUserId(String id, String userId);
}

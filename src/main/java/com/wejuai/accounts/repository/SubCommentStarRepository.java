package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.SubCommentStar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubCommentStarRepository extends MongoRepository<SubCommentStar, String> {

    boolean existsBySubCommentIdAndUserId(String id, String userId);

    SubCommentStar findBySubCommentIdAndUserId(String id, String userId);

}

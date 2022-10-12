package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.SubComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubCommentRepository extends MongoRepository<SubComment, String> {

    long countByAppCreatorOrRecipientAndWatchFalse(String appCreator, String recipient);
}

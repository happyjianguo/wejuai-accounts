package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.Remind;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RemindRepository extends MongoRepository<Remind, String> {

    long countByRecipientAndWatchFalse(String recipient);
}

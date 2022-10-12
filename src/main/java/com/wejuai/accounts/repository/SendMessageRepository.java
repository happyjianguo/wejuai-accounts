package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.SendMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ZM.Wang
 */
public interface SendMessageRepository extends MongoRepository<SendMessage, String> {

    Page<SendMessage> findByChatRelationId(String id, Pageable pageable);

    Page<SendMessage> findByRecipientOrSenderAndSenderOrRecipient(String r1, String s1, String s2, String r2, Pageable pageable);
}

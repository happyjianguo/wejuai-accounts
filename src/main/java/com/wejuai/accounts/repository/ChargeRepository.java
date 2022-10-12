package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.trade.Charge;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ZM.Wang
 */
public interface ChargeRepository extends MongoRepository<Charge, String> {
}

package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.LoginLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoginLogRepository extends MongoRepository<LoginLog, String> {

    List<LoginLog> findTop2ByAccountsIdOrderByCreatedAtDesc(String accountsId);
}

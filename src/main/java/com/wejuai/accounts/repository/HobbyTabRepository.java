package com.wejuai.accounts.repository;

import com.wejuai.entity.mongo.HobbyTab;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author ZM.Wang
 */
public interface HobbyTabRepository extends MongoRepository<HobbyTab, String> {

    List<HobbyTab> findByTab(String tab);

    List<HobbyTab> findByTabLike(String tab);
}

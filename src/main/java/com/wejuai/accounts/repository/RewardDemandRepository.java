package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.RewardDemand;
import com.wejuai.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface RewardDemandRepository extends JpaRepository<RewardDemand, String> {

    long countByUserAndDelFalse(User user);
}

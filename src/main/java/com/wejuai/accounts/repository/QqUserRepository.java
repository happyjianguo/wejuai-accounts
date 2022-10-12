package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.QqUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface QqUserRepository extends JpaRepository<QqUser, String> {

    QqUser findByOpenId(String openId);
}

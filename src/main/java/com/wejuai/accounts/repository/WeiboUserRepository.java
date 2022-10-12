package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.WeiboUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeiboUserRepository extends JpaRepository<WeiboUser, String> {

    WeiboUser findByOpenId(String openId);
}

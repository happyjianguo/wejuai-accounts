package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.WeixinUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeixinUserRepository extends JpaRepository<WeixinUser, String> {

    WeixinUser findByUnionId(String unionId);

    WeixinUser findByAppOpenId(String openId);
}

package com.wejuai.accounts.repository;


import com.wejuai.entity.mysql.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface AccountsRepository extends JpaRepository<Accounts, String> {

    Accounts findByEmail(String email);

    Accounts findByQqUser_OpenId(String qqOpenId);

    Accounts findByWeiboUser_OpenId(String uid);

    Accounts findByWeixinUser_UnionId(String unionId);

}

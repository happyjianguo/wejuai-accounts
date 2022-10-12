package com.wejuai.accounts.repository;


import com.wejuai.entity.mysql.Accounts;
import com.wejuai.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author ZM.Wang
 */
public interface UserRepository extends JpaRepository<User, String> {

    User findByAccounts(Accounts accounts);

    @Query(nativeQuery = true, value = "select ban from user where id=?1")
    boolean isBan(String userId);

    @Query(nativeQuery = true, value = "select del from user where id=?1")
    boolean isDel(String userId);

}

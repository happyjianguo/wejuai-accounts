package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.Title;
import com.wejuai.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface TitleRepository extends JpaRepository<Title, String> {

    long countByUser(User user);
}

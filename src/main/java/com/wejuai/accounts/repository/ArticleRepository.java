package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.Article;
import com.wejuai.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ZM.Wang
 */
public interface ArticleRepository extends JpaRepository<Article, String> {

    long countByUserAndDelFalseAndAuthorDelFalse(User user);

}

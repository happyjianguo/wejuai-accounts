package com.wejuai.accounts.repository;


import com.wejuai.entity.mysql.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface HobbyRepository extends JpaRepository<Hobby, String> {

}

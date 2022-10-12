package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.UserHobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHobbyRepository extends JpaRepository<UserHobby, String> {

    UserHobby findByUser(User user);
}

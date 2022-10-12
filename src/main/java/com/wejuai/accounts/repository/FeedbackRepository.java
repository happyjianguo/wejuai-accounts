package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
}

package com.wejuai.accounts.repository;

import com.wejuai.accounts.domain.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface EmailCodeRepository extends JpaRepository<EmailCode, String> {
}

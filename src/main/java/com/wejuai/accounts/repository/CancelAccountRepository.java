package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.CancelAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelAccountRepository extends JpaRepository<CancelAccount, String> {
}

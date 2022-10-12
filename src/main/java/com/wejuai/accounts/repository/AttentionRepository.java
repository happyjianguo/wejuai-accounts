package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.Attention;
import com.wejuai.entity.mysql.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface AttentionRepository extends JpaRepository<Attention, String> {

    long countByAttention(User attention);

    long countByFollow(User follow);

    boolean existsByAttentionAndFollow(User attention, User follow);

    Attention findByAttentionAndFollow(User attention, User follow);

    Page<Attention> findByAttention(User attention, Pageable pageable);

    Page<Attention> findByAttentionAndFollow_NickNameLike(User attention, String titleStr, Pageable pageable);

    Page<Attention> findByFollow(User follow, Pageable pageable);

}

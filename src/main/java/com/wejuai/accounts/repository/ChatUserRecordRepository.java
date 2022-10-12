package com.wejuai.accounts.repository;

import com.wejuai.entity.mysql.ChatUserRecord;
import com.wejuai.entity.mysql.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author ZM.Wang
 */
public interface ChatUserRecordRepository extends JpaRepository<ChatUserRecord, String> {

    Page<ChatUserRecord> findByRecipientAndDelFalse(User recipient, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT IFNULL(SUM(unread_msg_num),0) from chat_user_record where del=false and recipient_id=?1")
    long sumUserUnreadMsg(String userId);

    ChatUserRecord findByRecipientAndSender(User recipient, User sender);
}

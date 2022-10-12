package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ForbiddenException;
import com.wejuai.accounts.repository.ChatUserRecordRepository;
import com.wejuai.accounts.repository.SendMessageRepository;
import com.wejuai.accounts.repository.SystemMessageRepository;
import com.wejuai.accounts.repository.UserRepository;
import com.wejuai.accounts.web.dto.response.SystemMessageInfo;
import com.wejuai.dto.response.ChatUserRecordInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.SendMessage;
import com.wejuai.entity.mongo.SystemMessage;
import com.wejuai.entity.mysql.ChatUserRecord;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ZM.Wang
 * 聊天服务
 */
@Service
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final SendMessageRepository sendMessageRepository;
    private final SystemMessageRepository systemMessageRepository;
    private final ChatUserRecordRepository chatUserRecordRepository;

    private final MongoService mongoService;
    private final UserService userService;

    public MessageService(UserRepository userRepository, ChatUserRecordRepository chatUserRecordRepository, SendMessageRepository sendMessageRepository, SystemMessageRepository systemMessageRepository, MongoService mongoService, UserService userService) {
        this.userRepository = userRepository;
        this.chatUserRecordRepository = chatUserRecordRepository;
        this.sendMessageRepository = sendMessageRepository;
        this.systemMessageRepository = systemMessageRepository;
        this.mongoService = mongoService;
        this.userService = userService;
    }

    public Page<ChatUserRecordInfo> getChatRelationRecord(User user, Pageable pageable) {
        return chatUserRecordRepository.findByRecipientAndDelFalse(user, pageable).map(ChatUserRecordInfo::new);
    }

    @Transactional
    public Slice<SendMessage> getMessageByChatRelation(User user, String otherUserId, long page, long size) {
        User other = userService.getUser(otherUserId);
        ChatUserRecord chatUserRecord = chatUserRecordRepository.findByRecipientAndSender(user, other);
        if (chatUserRecord == null) {
            chatUserRecord = chatUserRecordRepository.save(new ChatUserRecord(other, user, ""));
            ChatUserRecord chatUserRecord2 = chatUserRecordRepository.findByRecipientAndSender(other, user);
            if (chatUserRecord2 == null) {
                chatUserRecordRepository.save(new ChatUserRecord(user, other, ""));
            }
        }
        if (!chatUserRecord.getRecipient().equals(user)) {
            logger.warn("该账号不属于该用户, userId: {}, chatUserRecordId: {}", user.getId(), chatUserRecord.getId());
            throw new ForbiddenException("该聊天关系不属于你");
        }
        String recipient = chatUserRecord.getRecipient().getId();
        String sender = chatUserRecord.getSender().getId();
        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria();
        criteria1.and("sender").is(recipient);
        criteria1.and("recipient").is(sender);
        Criteria criteria2 = new Criteria();
        criteria2.and("sender").is(sender);
        criteria2.and("recipient").is(recipient);
        criteria.orOperator(criteria1, criteria2);
        long count = mongoService.getMongoPageCount(criteria, SendMessage.class);
        List<SendMessage> sendMessages = mongoService.getList(criteria, page, size, SendMessage.class, Sort.Direction.DESC, "createdAt");
        List<SendMessage> sendMessages2 = new ArrayList<>();
        for (int i = sendMessages.size(); i > 0; i--) {
            sendMessages2.add(sendMessages.get(i - 1));
        }
        long unreadMsgNum = chatUserRecord.getUnreadMsgNum();
        chatUserRecordRepository.save(chatUserRecord.watchMsg());
        userRepository.save(user.watchMsg(unreadMsgNum));
        return new Slice<>(sendMessages2, page, size, count);
    }

    public Slice<SystemMessageInfo> getSystemMessage(User user, Pageable pageable) {
        Page<SystemMessage> page = systemMessageRepository.findByUserId(user.getId(), pageable);
        List<SystemMessage> content = page.getContent();
        List<SystemMessage> content2 = new ArrayList<>();
        for (int i = content.size(); i > 0; i--) {
            content2.add(content.get(i - 1));
        }
        return new Slice<>(content2.stream().map(SystemMessageInfo::new).collect(Collectors.toList()),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements());
    }

    @Transactional
    public void watchMessage(User user, String id) {
        SendMessage sendMessage = sendMessageRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该条消息: " + id));
        if (!StringUtils.equals(sendMessage.getRecipient(), user.getId())) {
            logger.warn("该账号不属于该用户, userId: {}, chatUserRecordId: {}", user.getId(), id);
            throw new ForbiddenException("该聊天消息不属于你");
        }
        ChatUserRecord chatUserRecord = chatUserRecordRepository.findById(sendMessage.getChatRelationId()).orElseGet(() -> {
            User sender = userService.getUser(sendMessage.getSender());
            User recipient = userService.getUser(sendMessage.getRecipient());
            ChatUserRecord supplement = new ChatUserRecord(sendMessage.getChatRelationId(), sender, recipient, sendMessage.getText());
            return chatUserRecordRepository.save(supplement);
        });
        chatUserRecordRepository.save(chatUserRecord.watchMsg(1));
        userRepository.save(user.watchMsg());
    }

    public void watchSystemMessage(User user, String id) {
        Optional<SystemMessage> systemMessageOptional = systemMessageRepository.findById(id);
        if (systemMessageOptional.isEmpty()) {
            throw new BadRequestException("没有该系统消息");
        }
        SystemMessage systemMessage = systemMessageOptional.get();
        if (!systemMessage.getUserId().equals(user.getId())) {
            throw new BadRequestException("该消息不属于你");
        }
        userRepository.save(user.watchMsg());
        logger.info("修改完用户消息数");
        systemMessageRepository.save(systemMessage.setWatch(true));
        logger.info("修改完消息已读状态");
    }

}

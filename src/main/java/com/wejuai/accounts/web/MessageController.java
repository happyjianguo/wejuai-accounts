package com.wejuai.accounts.web;

import com.wejuai.accounts.service.MessageService;
import com.wejuai.accounts.web.dto.response.SystemMessageInfo;
import com.wejuai.dto.response.ChatUserRecordInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.SendMessage;
import com.wejuai.entity.mongo.SystemMessage;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "消息服务")
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation("获取聊天关系记录")
    @GetMapping("/chatRelation")
    public Page<ChatUserRecordInfo> getChatRelationRecord(@SessionAttribute(SESSION_LOGIN) User user,
                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return messageService.getChatRelationRecord(user, pageable);
    }

    @ApiOperation("根据对方userId获取聊天消息")
    @GetMapping("/{userId}/user")
    public Slice<SendMessage> getMessageByChatRelation(@PathVariable String userId,
                                                       @SessionAttribute(SESSION_LOGIN) User user,
                                                       @RequestParam(required = false, defaultValue = "0") long page,
                                                       @RequestParam(required = false, defaultValue = "10") long size) {
        return messageService.getMessageByChatRelation(user, userId, page, size);
    }

    @ApiOperation("系统消息列表")
    @GetMapping("/system")
    public Slice<SystemMessageInfo> getSystemMessage(@SessionAttribute(SESSION_LOGIN) User user,
                                                     @RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return messageService.getSystemMessage(user, pageable);
    }

    @ApiOperation("阅读直接推送来的消息")
    @PutMapping("/{id}/watch")
    public void watchMessage(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        messageService.watchMessage(user, id);
    }

    @ApiOperation("阅读系统消息")
    @PutMapping("/system/{id}/watch")
    public void watchSystemMessage(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        messageService.watchSystemMessage(user, id);
    }
}

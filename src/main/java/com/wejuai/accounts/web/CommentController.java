package com.wejuai.accounts.web;

import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.service.CommentService;
import com.wejuai.accounts.web.dto.request.CreateCommentRequest;
import com.wejuai.accounts.web.dto.request.CreateRemindsRequest;
import com.wejuai.accounts.web.dto.request.CreateSubCommentRequest;
import com.wejuai.dto.request.CommentType;
import com.wejuai.dto.request.SaveCommentRequest;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "评论相关")
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public CommentController(CommentService commentService, WejuaiCoreClient wejuaiCoreClient) {
        this.commentService = commentService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("创建一级评论")
    @PostMapping
    public void comment(@SessionAttribute(SESSION_LOGIN) String userId, @RequestBody @Valid CreateCommentRequest request) {
        SaveCommentRequest coreReq = new SaveCommentRequest(userId, request.getAppType(), request.getAppId(),
                request.getText());
        wejuaiCoreClient.createComment(coreReq);
    }

    @ApiOperation("创建二级评论")
    @PostMapping("/sub")
    public void createSubComment(@SessionAttribute(SESSION_LOGIN) User user, @RequestBody @Valid CreateSubCommentRequest request) {
        wejuaiCoreClient.createSubComment(request.getCoreReq(user));
    }

    @ApiOperation("创建艾特记录")
    @PostMapping("/remind")
    public void createReminds(@SessionAttribute(SESSION_LOGIN) User user, @RequestBody @Valid CreateRemindsRequest request) {
        wejuaiCoreClient.createReminds(request.getCoreReq(user));
    }

    @ApiOperation("删除一级评论")
    @DeleteMapping("/{id}")
    public void removeComments(@SessionAttribute(SESSION_LOGIN) String userId, @PathVariable String id) {
        wejuaiCoreClient.removeComment(id, userId);
    }

    @ApiOperation("删除二级评论")
    @DeleteMapping("/sub/{id}")
    public void removeSubComment(@SessionAttribute(SESSION_LOGIN) String userId, @PathVariable String id) {
        wejuaiCoreClient.removeSubComment(id, userId);
    }

    @ApiOperation("点赞评论")
    @PutMapping("/star/{id}")
    public void starComment(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        commentService.starComment(user, id);
    }

    @ApiOperation("取消点赞评论")
    @PutMapping("/unStar/{id}")
    public void unStarComment(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        commentService.unStarComment(user, id);
    }

    @ApiOperation("点赞二级评论")
    @PutMapping("/sub/star/{id}")
    public void starSubComment(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        commentService.starSubComment(user, id);
    }

    @ApiOperation("取消点赞二级评论")
    @PutMapping("/sub/unStar/{id}")
    public void unStarSubComment(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        commentService.unStarSubComment(user, id);
    }

    @ApiOperation("阅读评论")
    @PutMapping("/watch/{id}")
    public void watchComment(@SessionAttribute(SESSION_LOGIN) String userId,
                             @PathVariable String id,
                             @RequestParam CommentType type) {
        wejuaiCoreClient.watchComment(userId, id, type);
    }

}

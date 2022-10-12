package com.wejuai.accounts.web;

import com.endofmaster.rest.exception.ForbiddenException;
import com.wejuai.accounts.infrastructure.client.GitlabClient;
import com.wejuai.accounts.service.FeedbackService;
import com.wejuai.accounts.web.dto.request.CreateFeedbackRequest;
import com.wejuai.accounts.web.dto.request.CreateHobbyApplyRequest;
import com.wejuai.accounts.web.dto.request.CreateReportRequest;
import com.wejuai.accounts.web.dto.request.PushWebLogRequest;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "反馈相关")
@RestController
@RequestMapping("/feedBack")
public class FeedBackController {

    private final FeedbackService feedbackService;
    private final GitlabClient gitlabClient;

    public FeedBackController(FeedbackService feedbackService, GitlabClient gitlabClient) {
        this.feedbackService = feedbackService;
        this.gitlabClient = gitlabClient;
    }

    @ApiOperation("意见反馈")
    @PostMapping("/opinion")
    public void saveFeedback(@RequestBody @Valid CreateFeedbackRequest request,
                             @SessionAttribute(value = SESSION_LOGIN, required = false) String userId) {
        feedbackService.saveFeedback(request, userId);
    }

    @ApiOperation("爱好申请")
    @PostMapping("/hobbyApply")
    public void saveHobbyApply(@RequestBody @Valid CreateHobbyApplyRequest request,
                               @SessionAttribute(value = SESSION_LOGIN, required = false) String userId) {
        feedbackService.saveHobbyApply(request, userId);
    }

    @ApiOperation("前段提交错误日志")
    @PostMapping("/pushWebErrorLog")
    public void pushWebErrorLog(@RequestParam String token, @RequestBody PushWebLogRequest request) {
        if (!StringUtils.equals("wejuai-web-error.12jk3n", token)) {
            throw new ForbiddenException("You don't have permission");
        }
        String random = RandomStringUtils.randomNumeric(6);
        String filePath = LocalDate.now() + "/" + LocalTime.now().toString() + "-" + random;
        new Thread(() -> gitlabClient.pushNewFile(filePath, request.setCommentMsg("前段提交日志" + random))).start();
    }

    @ApiOperation("举报")
    @PostMapping("/report")
    public void report(@SessionAttribute(value = SESSION_LOGIN, required = false) String userId,
                       @RequestBody @Valid CreateReportRequest request) {
        feedbackService.report(userId, request);
    }

}

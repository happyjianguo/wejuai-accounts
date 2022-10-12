package com.wejuai.accounts.web;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.service.EvaluateService;
import com.wejuai.accounts.service.HobbyService;
import com.wejuai.accounts.web.dto.request.CreateApplyCancelRewardDemandRequest;
import com.wejuai.accounts.web.dto.request.CreateEvaluateRequest;
import com.wejuai.accounts.web.dto.request.CreateRewardDemandRequest;
import com.wejuai.accounts.web.dto.request.CreateRewardSubmissionRequest;
import com.wejuai.accounts.web.dto.request.UpdateRewardSubmissionDraftRequest;
import com.wejuai.dto.request.AppAddTextRequest;
import com.wejuai.dto.request.ArticleRevokeRequest;
import com.wejuai.dto.request.SaveRewardSubmissionDraftRequest;
import com.wejuai.dto.response.RewardDemandListInfo;
import com.wejuai.dto.response.RewardSubmissionDraftInfo;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mysql.GiveType;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;
import static com.wejuai.entity.mongo.AppType.ARTICLE;
import static com.wejuai.entity.mongo.AppType.REWARD_DEMAND;

/**
 * @author ZM.Wang
 */
@Api(tags = "悬赏相关")
@RestController
@RequestMapping("/api/rewardDemand")
public class RewardDemandController {

    private final HobbyService hobbyService;
    private final EvaluateService evaluateService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public RewardDemandController(HobbyService hobbyService, EvaluateService evaluateService, WejuaiCoreClient wejuaiCoreClient) {
        this.hobbyService = hobbyService;
        this.evaluateService = evaluateService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("创建悬赏")
    @PostMapping
    public void saveRewardDemand(@SessionAttribute(SESSION_LOGIN) User user,
                                 @RequestBody @Valid CreateRewardDemandRequest request) {
        Hobby hobby = hobbyService.getHobby(request.getHobbyId());
        hobbyService.checkUserHasHobby(user, hobby);
        wejuaiCoreClient.saveRewardDemand(request.getCoreReq(user));
    }

    @ApiOperation("用户收藏悬赏列表")
    @GetMapping("/collect")
    public Slice<RewardDemandListInfo> findRewardDemandByGiveType(@SessionAttribute(SESSION_LOGIN) User user,
                                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.findRewardDemandByGiveType(user.getId(), GiveType.COLLECT, page, size);
    }

    @ApiOperation("增加悬赏金")
    @PutMapping("/{id}/addReward")
    public void addReward(@SessionAttribute(SESSION_LOGIN) String userId,
                          @PathVariable String id, @RequestParam @ApiParam(value = "悬赏金", example = "0") long integral) {
        if (integral < 1) {
            throw new BadRequestException("增加赏金必须大于0");
        }
        wejuaiCoreClient.addReward(id, userId, integral);
    }

    @ApiOperation("删除悬赏")
    @DeleteMapping("/{id}")
    public void deleteRewardDemand(@PathVariable String id, @SessionAttribute(SESSION_LOGIN) String userId) {
        wejuaiCoreClient.deleteRewardDemand(id, userId);
    }

    @ApiOperation("提交悬赏内容")
    @PostMapping("/result")
    public void saveRewardDemandResult(@SessionAttribute(SESSION_LOGIN) User user,
                                       @RequestBody @Valid CreateRewardSubmissionRequest request) {
        wejuaiCoreClient.saveRewardDemandResult(request.getCoreReq(user));
    }

    @ApiOperation("选定结果")
    @PostMapping("/selected/{id}")
    public void selectedResult(@PathVariable String id, @SessionAttribute(SESSION_LOGIN) String userId) {
        wejuaiCoreClient.selectedResult(id, userId);
    }

    @ApiOperation("延时悬赏")
    @PutMapping("/{id}/extension")
    public void extensionRewardDemand(@PathVariable String id, @SessionAttribute(SESSION_LOGIN) String userId) {
        wejuaiCoreClient.extensionRewardDemand(id, userId);
    }

    @ApiOperation("申请全额退款取消悬赏")
    @PostMapping("/applyCancel")
    public void applyCancelRewardDemand(@SessionAttribute(SESSION_LOGIN) User user,
                                        @RequestBody @Valid CreateApplyCancelRewardDemandRequest request) {
        wejuaiCoreClient.applyCancelRewardDemand(request.getCoreReq(user));
    }

    @ApiOperation("给提交答案者评分")
    @PostMapping("/evaluate")
    public void evaluate(@SessionAttribute(SESSION_LOGIN) User user,
                         @RequestBody @Valid CreateEvaluateRequest request) {
        evaluateService.evaluate(user, request, AppType.REWARD_DEMAND);
    }

    @ApiOperation("用户给悬赏点赞")
    @PutMapping("/giveStar/{id}")
    public void giveStar(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.giveNum(user.getId(), id, GiveType.STAR, REWARD_DEMAND);
    }

    @ApiOperation("用户取消悬赏点赞")
    @PutMapping("/reduceStar/{id}")
    public void reduceStar(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.reduceNum(user.getId(), id, GiveType.STAR, REWARD_DEMAND);
    }

    @ApiOperation("收藏悬赏")
    @PutMapping("/giveCollect/{id}")
    public void giveCollect(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.giveNum(user.getId(), id, GiveType.COLLECT, REWARD_DEMAND);
    }

    @ApiOperation("取消收藏悬赏")
    @PutMapping("/reduceCollect/{id}")
    public void reduceCollect(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.reduceNum(user.getId(), id, GiveType.COLLECT, REWARD_DEMAND);
    }

    @ApiOperation("撤回回答")
    @PostMapping("/rewardSubmission/{id}/revoke")
    public void rewardSubmissionRevoke(@PathVariable String id, @SessionAttribute(SESSION_LOGIN) String userId) {
        wejuaiCoreClient.rewardSubmissionRevoke(id, new ArticleRevokeRequest(userId, false, null));
    }

    @ApiOperation("悬赏回答草稿列表")
    @GetMapping("/rewardSubmission/draft")
    public Slice<RewardSubmissionDraftInfo> getRewardSubmissionDrafts(@SessionAttribute(SESSION_LOGIN) String userId,
                                                                      @RequestParam(required = false, defaultValue = "0") int page,
                                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getRewardSubmissionDrafts(userId, page, size);
    }

    @ApiOperation("悬赏回答草稿详情")
    @GetMapping("/rewardSubmission/draft/{draftId}")
    public RewardSubmissionDraftInfo rewardSubmissionDraftDetails(@SessionAttribute(SESSION_LOGIN) String userId, @PathVariable String draftId) {
        return wejuaiCoreClient.rewardSubmissionDraftDetails(draftId, userId);
    }

    @ApiOperation("修改悬赏草稿")
    @PutMapping("/rewardSubmission/draft/{draftId}")
    public void saveRewardSubmissionDraft(@SessionAttribute(SESSION_LOGIN) String userId, @PathVariable String draftId,
                                          @RequestBody @Valid UpdateRewardSubmissionDraftRequest request) {
        SaveRewardSubmissionDraftRequest coreReq = new SaveRewardSubmissionDraftRequest(draftId, userId, request.getInShort(), request.getText());
        wejuaiCoreClient.saveRewardSubmissionDraft(coreReq);
    }

    @ApiOperation("草稿发布为悬赏回答")
    @PostMapping("/rewardSubmission/draft/{draftId}/publish")
    public void publishRewardSubmission(@SessionAttribute(SESSION_LOGIN) String userId, @PathVariable String draftId) {
        wejuaiCoreClient.publishRewardSubmission(draftId, userId);
    }

    @ApiOperation("删除悬赏回答草稿")
    @DeleteMapping("/rewardSubmission/draft/{draftId}")
    public void delRewardSubmissionDraft(@SessionAttribute(SESSION_LOGIN) String userId, @PathVariable String draftId) {
        wejuaiCoreClient.delRewardSubmissionDraft(draftId, userId);
    }

    @ApiOperation("添加内容")
    @PutMapping("/addText")
    public void addRewardText(@SessionAttribute(SESSION_LOGIN) String userId,
                              @RequestBody @Valid AppAddTextRequest request) {
        wejuaiCoreClient.addRewardText(userId, request);
    }

    @ApiOperation("阅读悬赏")
    @PutMapping("/watch/{id}")
    public void watch(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.giveNum(user.getId(), id, GiveType.DISPLAY, REWARD_DEMAND);
    }
}

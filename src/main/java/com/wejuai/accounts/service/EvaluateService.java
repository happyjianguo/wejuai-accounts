package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.web.dto.request.CreateEvaluateRequest;
import com.wejuai.dto.request.SaveEvaluateRequest;
import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author ZM.Wang
 */
@Service
public class EvaluateService {

    private final WejuaiCoreClient wejuaiCoreClient;

    public EvaluateService(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    public void evaluate(User user, CreateEvaluateRequest request, AppType type) {
        if (type == AppType.ARTICLE) {
            if (StringUtils.isBlank(request.getAppId())) {
                throw new BadRequestException("文章id必填");
            }
            SaveEvaluateRequest coreReq = new SaveEvaluateRequest(user.getId(), request.getScore(), request.getContent()).article(request.getAppId());
            wejuaiCoreClient.evaluate(coreReq);
        } else if (type == AppType.REWARD_DEMAND) {
            if (StringUtils.isBlank(request.getRewardSubmissionId())) {
                throw new BadRequestException("悬赏答案id必填");
            }
            SaveEvaluateRequest coreReq = new SaveEvaluateRequest(user.getId(), request.getScore(), request.getContent()).rewardSubmission(request.getRewardSubmissionId());
            wejuaiCoreClient.evaluate(coreReq);
        } else {
            throw new BadRequestException("暂无其他类型的评分");
        }
    }
}

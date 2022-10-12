package com.wejuai.accounts.infrastructure.client;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import com.wejuai.dto.request.*;
import com.wejuai.dto.response.*;
import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mongo.trade.TradeStatus;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.ChannelQueryType;
import com.wejuai.entity.mysql.GiveType;
import com.wejuai.entity.mysql.OrdersType;
import com.wejuai.entity.mysql.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZM.Wang
 */
public class WejuaiCoreClient {

    private final RestTemplate restTemplate;
    private final String url;

    public WejuaiCoreClient(RestTemplate restTemplate, String url) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public void saveArticle(SaveArticleRequest request) {
        try {
            restTemplate.postForObject(url + "/app/article", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public KeyValue saveArticleDraft(SaveArticleDraftRequest request) {
        try {
            return restTemplate.postForObject(url + "/app/article/articleDraft", request, KeyValue.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void publishArticle(User user, String id) {
        try {
            restTemplate.postForObject(url + "/app/article/publish/{id}?userId={userId}", null, Void.class, id, user.getId());
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void revokeArticle(String id, ArticleRevokeRequest request) {
        try {
            restTemplate.postForObject(url + "/app/article/{id}/revoke", request, Void.class, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void addArticleText(String userId, AppAddTextRequest request) {
        try {
            restTemplate.put(url + "/app/article/addText?userId={userId}", request, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void updateArticleIntegral(String id, String userId, long integral) {
        try {
            restTemplate.put(url + "/app/article/{id}/integral?userId={userId}&integral={integral}", null, id, userId, integral);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void deleteArticle(User user, String id) {
        try {
            restTemplate.delete(url + "/app/article/{id}?userId={userId}", id, user.getId());
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void buyArticle(User user, String id) {
        try {
            restTemplate.getForObject(url + "/app/article/{id}/buy?userId={userId}", Void.class, id, user.getId());
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void deleteArticleDraft(User user, String id) {
        try {
            restTemplate.delete(url + "/app/article/articleDraft/{id}?userId={userId}", id, user.getId());
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<ArticleListInfo> getArticleDrafts(String userId, String hobbyId, int page, int size) {
        try {
            return restTemplate.exchange(url + "/app/article/articleDraft?userId={userId}&hobbyId={hobbyId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<ArticleListInfo>>() {
                    }, userId, hobbyId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public ArticleInfo getArticleDraft(String id) {
        try {
            return restTemplate.getForObject(url + "/app/article/articleDraft/{id}", ArticleInfo.class, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<ArticleListInfo> findArticleByGiveType(String userId, GiveType giveType, int page, int size) {
        try {
            return restTemplate.exchange(url + "/app/article/{userId}/give?page={page}&size={size}&giveType={giveType}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<ArticleListInfo>>() {
                    }, userId, page, size, giveType).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void saveRewardDemand(SaveRewardDemandRequest request) {
        try {
            restTemplate.postForObject(url + "/app/rewardDemand", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<RewardDemandListInfo> findRewardDemandByGiveType(String userId, GiveType giveType, int page, int size) {
        try {
            return restTemplate.exchange(url + "/app/rewardDemand/{userId}/give?page={page}&size={size}&giveType={giveType}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<RewardDemandListInfo>>() {
                    }, userId, page, size, giveType).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void addReward(String id, String userId, long integral) {
        try {
            restTemplate.put(url + "/app/rewardDemand/{id}/addReward?userId={userId}&integral={integral}", null, id, userId, integral);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void addRewardText(String userId, AppAddTextRequest request) {
        try {
            restTemplate.put(url + "/app/rewardDemand/addText?userId={userId}", request, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void deleteRewardDemand(String id, String userId) {
        try {
            restTemplate.delete(url + "/app/rewardDemand/{id}?userId={userId}", id, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void saveRewardDemandResult(SaveRewardSubmissionRequest request) {
        try {
            restTemplate.postForObject(url + "/app/rewardDemand/result", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void selectedResult(String id, String userId) {
        try {
            restTemplate.postForObject(url + "/app/rewardDemand/selected/{id}?userId={userId}", null, Void.class, id, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void extensionRewardDemand(String id, String userId) {
        try {
            restTemplate.put(url + "/app/rewardDemand/{id}/extension?userId={userId}", null, id, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void applyCancelRewardDemand(ApplyCancelRewardDemandRequest request) {
        try {
            restTemplate.postForObject(url + "/app/rewardDemand/applyCancel", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void giveNum(String userId, String articleId, GiveType type, AppType appType) {
        try {
            restTemplate.put(url + "/app/give/{id}?type={type}&userId={userId}&appType={appType}", null, articleId, type, userId, appType);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void reduceNum(String userId, String articleId, GiveType type, AppType appType) {
        try {
            restTemplate.put(url + "/app/reduce/{id}?type={type}&userId={userId}&appType={appType}", null, articleId, type, userId, appType);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void createComment(SaveCommentRequest request) {
        try {
            restTemplate.postForObject(url + "/comment", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void watchComment(String userId, String id, CommentType type) {
        try {
            restTemplate.put(url + "/comment/watch/{id}?userId={userId}&type={type}", null, id, userId, type);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void createSubComment(SaveSubCommentRequest request) {
        try {
            restTemplate.postForObject(url + "/comment/sub", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void createReminds(SaveRemindsRequest request) {
        try {
            restTemplate.postForObject(url + "/comment/remind", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void removeComment(String id, String userId) {
        try {
            restTemplate.delete(url + "/comment/{id}?userId={userId}", id, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void removeSubComment(String id, String userId) {
        try {
            restTemplate.delete(url + "/comment/{id}/sub?userId={userId}", id, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void evaluate(SaveEvaluateRequest request) {
        try {
            restTemplate.postForObject(url + "/evaluate", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void withdrawal(SaveWithdrawalRequest request) {
        try {
            restTemplate.postForObject(url + "/orders/withdrawal", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void followHobby(User user, String id) {
        try {
            restTemplate.postForObject(url + "/hobby/follow/{id}?userId={userId}", null, Void.class, id, user.getId());
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void unfollowHobby(User user, String id) {
        try {
            restTemplate.postForObject(url + "/hobby/unfollow/{id}?userId={userId}", null, Void.class, id, user.getId());
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public GetHobbyDomain getHobbyDomainByTab(String userId, String tab) {
        try {
            return restTemplate.getForObject(url + "/hobby/tab?userId={userId}&tab={tab}", GetHobbyDomain.class, userId, tab);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void orderAppeal(SaveOrderAppealRequest request) {
        try {
            restTemplate.postForObject(url + "/orders/orderAppeal", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<OrderAppealInfo> getOrderAppeals(String userId, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders/orderAppeal?userId={userId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<OrderAppealInfo>>() {
                    }, userId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<OrdersInfo> getOrders(String userId, OrdersType type, Boolean income, Long start, Long end, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders?userId={userId}&type={type}&income={income}&start={start}&end={end}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<OrdersInfo>>() {
                    }, userId, type, income, start, end, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<ArticleListInfo> getPurchased(String userId, String titleStr, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders/purchased?userId={userId}&titleStr={titleStr}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<ArticleListInfo>>() {
                    }, userId, titleStr, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<ChargeListInfo> getChargeInfos(String userId, TradeStatus status, ChannelQueryType channelType, Long start, Long end, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders/charge?userId={userId}&status={status}&channelType={channelType}&page={page}&size={size}&start={start}&end={end}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<ChargeListInfo>>() {
                    }, userId, status, channelType, page, size, start, end).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<WithdrawalInfo> getWithdrawals(String userId, ApplyStatus status, Long start, Long end, int page, int size) {
        try {
            return restTemplate.exchange(url + "/orders/withdrawal?userId={userId}&status={status}&page={page}&size={size}&start={start}&end={end}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<WithdrawalInfo>>() {
                    }, userId, status, page, size, start, end).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public UserIntegralInfo getUserIntegral(String userId) {
        try {
            return restTemplate.getForObject(url + "/userIntegral/{userId}", UserIntegralInfo.class, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void rewardSubmissionRevoke(String id, ArticleRevokeRequest request) {
        try {
            restTemplate.postForObject(url + "/app/rewardDemand/rewardSubmission/{id}/revoke", request, Void.class, id);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public Slice<RewardSubmissionDraftInfo> getRewardSubmissionDrafts(String userId, int page, int size) {
        try {
            return restTemplate.exchange(url + "/app/rewardDemand/rewardSubmission/draft?userId={userId}&page={page}&size={size}", HttpMethod.GET,
                    null, new ParameterizedTypeReference<Slice<RewardSubmissionDraftInfo>>() {
                    }, userId, page, size).getBody();
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public RewardSubmissionDraftInfo rewardSubmissionDraftDetails(String draftId, String userId) {
        try {
            return restTemplate.getForObject(url + "/app/rewardDemand/rewardSubmission/draft/{draftId}?userId={userId}",
                    RewardSubmissionDraftInfo.class, draftId, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void publishRewardSubmission(String draftId, String userId) {
        try {
            restTemplate.postForObject(url + "/app/rewardDemand/rewardSubmission/draft/{draftId}/publish?userId={userId}",
                    null, Void.class, draftId, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void saveRewardSubmissionDraft(SaveRewardSubmissionDraftRequest request) {
        try {
            restTemplate.put(url + "/app/rewardDemand/rewardSubmission/draft", request, Void.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void delRewardSubmissionDraft(String draftId, String userId) {
        try {
            restTemplate.delete(url + "/app/rewardDemand/rewardSubmission/draft/{draftId}?userId={userId}",
                    draftId, userId);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public IdBaseResponse saveCelestialBody(SaveCelestialBodyRequest request) {
        try {
            return restTemplate.postForObject(url + "/celestialBody", request, IdBaseResponse.class);
        } catch (HttpServerErrorException e) {
            throw new ServerException(e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e.getResponseBodyAsString());
        }
    }

    public void originTest() {
        restTemplate.getForObject(url + "/test/accounts", Void.class);
    }


    public void testConnect(String url) {
        restTemplate.getForObject(url + "/test/testConnect", Void.class);
    }

}

package com.wejuai.accounts.web;

import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.service.ArticleService;
import com.wejuai.accounts.service.EvaluateService;
import com.wejuai.accounts.web.dto.request.CreateArticleDraftRequest;
import com.wejuai.accounts.web.dto.request.CreateArticleRequest;
import com.wejuai.accounts.web.dto.request.CreateEvaluateRequest;
import com.wejuai.accounts.web.dto.response.StarAndCollection;
import com.wejuai.dto.request.AppAddTextRequest;
import com.wejuai.dto.request.ArticleRevokeRequest;
import com.wejuai.dto.response.ArticleInfo;
import com.wejuai.dto.response.ArticleListInfo;
import com.wejuai.dto.response.KeyValue;
import com.wejuai.dto.response.Slice;
import com.wejuai.entity.mongo.AppType;
import com.wejuai.entity.mysql.GiveType;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;
import static com.wejuai.entity.mongo.AppType.ARTICLE;

/**
 * Created by ZM.Wang
 */
@Api(tags = "文章相关")
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final EvaluateService evaluateService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public ArticleController(ArticleService articleService, EvaluateService evaluateService, WejuaiCoreClient wejuaiCoreClient) {
        this.articleService = articleService;
        this.evaluateService = evaluateService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("保存文章")
    @PostMapping
    public void saveArticle(@SessionAttribute(SESSION_LOGIN) User user,
                            @RequestBody @Valid CreateArticleRequest request) {
        articleService.saveArticle(user, request);
    }

    @ApiOperation("更新文章封面")
    @PutMapping("/{id}/cover")
    public void updateCover(@SessionAttribute(SESSION_LOGIN) User user,
                            @PathVariable String id, @RequestParam String imageId) {
        articleService.updateCover(user, id, imageId);
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/{id}")
    public void deleteArticle(@SessionAttribute(SESSION_LOGIN) User user,
                              @PathVariable String id) {
        wejuaiCoreClient.deleteArticle(user, id);
    }

    @ApiOperation("购买文章")
    @GetMapping("/{id}/buy")
    public void buyArticle(@SessionAttribute(SESSION_LOGIN) User user,
                           @PathVariable String id) {
        wejuaiCoreClient.buyArticle(user, id);
    }

    @ApiOperation("删除文章草稿")
    @DeleteMapping("/articleDraft/{id}")
    public void deleteArticleDraft(@SessionAttribute(SESSION_LOGIN) User user,
                                   @PathVariable String id) {
        wejuaiCoreClient.deleteArticleDraft(user, id);
    }

    @ApiOperation("保存文章草稿")
    @PostMapping("/articleDraft")
    public KeyValue saveArticleDraft(@SessionAttribute(SESSION_LOGIN) User user,
                                     @RequestBody @Valid CreateArticleDraftRequest request) {
        return articleService.saveArticleDraft(user, request);
    }

    @ApiOperation("发布文章草稿为正式")
    @PostMapping("/publish/{id}")
    public void publishArticle(@SessionAttribute(SESSION_LOGIN) User user,
                               @ApiParam("草稿id") @PathVariable String id) {
        wejuaiCoreClient.publishArticle(user, id);
    }

    @ApiOperation("查看当前文章当前用户是否收藏和点赞")
    @GetMapping("/{id}")
    public StarAndCollection getStarAndCollection(@SessionAttribute(SESSION_LOGIN) User user,
                                                  @PathVariable String id) {
        return articleService.getStarAndCollection(user, id);
    }

    @ApiOperation("获取草稿列表")
    @GetMapping("/articleDraft")
    public Slice<ArticleListInfo> getArticleDrafts(@SessionAttribute(SESSION_LOGIN) User user,
                                                   @RequestParam(required = false, defaultValue = "") String hobbyId,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getArticleDrafts(user.getId(), hobbyId, page, size);
    }

    @ApiOperation("获取文章草稿详情")
    @GetMapping("/articleDraft/{id}")
    public ArticleInfo getArticleDraft(@SessionAttribute(SESSION_LOGIN) User user,
                                       @PathVariable String id) {
        return articleService.getArticleDraft(user, id);
    }

    @ApiOperation("获取用户点赞的文章列表")
    @GetMapping("/star")
    public Slice<ArticleListInfo> getArticlesByStar(@SessionAttribute(SESSION_LOGIN) User user,
                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.findArticleByGiveType(user.getId(), GiveType.STAR, page, size);
    }

    @ApiOperation("获取用户收藏的文章列表")
    @GetMapping("/collect")
    public Slice<ArticleListInfo> getArticlesByCollection(@SessionAttribute(SESSION_LOGIN) User user,
                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.findArticleByGiveType(user.getId(), GiveType.COLLECT, page, size);
    }

    @ApiOperation("用户给文章点赞")
    @PutMapping("/giveStar/{id}")
    public void giveStar(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.giveNum(user.getId(), id, GiveType.STAR, ARTICLE);
    }

    @ApiOperation("用户取消文章点赞")
    @PutMapping("/reduceStar/{id}")
    public void reduceStar(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.reduceNum(user.getId(), id, GiveType.STAR, ARTICLE);
    }

    @ApiOperation("收藏文章")
    @PutMapping("/giveCollect/{id}")
    public void giveCollect(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.giveNum(user.getId(), id, GiveType.COLLECT, ARTICLE);
    }

    @ApiOperation("取消收藏文章")
    @PutMapping("/reduceCollect/{id}")
    public void reduceCollect(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.reduceNum(user.getId(), id, GiveType.COLLECT, ARTICLE);
    }

    @ApiOperation("阅读文章")
    @PutMapping("/watch/{id}")
    public void watch(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.giveNum(user.getId(), id, GiveType.DISPLAY, ARTICLE);
    }

    @ApiOperation("给作者评分")
    @PostMapping("/evaluate")
    public void evaluate(@SessionAttribute(SESSION_LOGIN) User user,
                         @RequestBody @Valid CreateEvaluateRequest request) {
        evaluateService.evaluate(user, request, AppType.ARTICLE);
    }

    @ApiOperation("撤销发布文章")
    @PostMapping("/{id}/revoke")
    public void revokeArticle(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.revokeArticle(id, new ArticleRevokeRequest(user.getId(), false, null));
    }

    @ApiOperation("添加内容")
    @PutMapping("/addText")
    public void addRewardText(@SessionAttribute(SESSION_LOGIN) String userId,
                              @RequestBody @Valid AppAddTextRequest request) {
        wejuaiCoreClient.addArticleText(userId, request);
    }

    @ApiOperation("修改积分")
    @PutMapping("/{id}/integral")
    public void updateArticleIntegral(@PathVariable String id,
                                      @SessionAttribute(SESSION_LOGIN) String userId,
                                      @ApiParam("积分") @RequestParam long integral) {
        wejuaiCoreClient.updateArticleIntegral(id, userId, integral);
    }
}

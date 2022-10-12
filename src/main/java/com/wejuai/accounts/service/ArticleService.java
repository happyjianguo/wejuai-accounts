package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ForbiddenException;
import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.repository.ArticleRepository;
import com.wejuai.accounts.repository.CollectionRepository;
import com.wejuai.accounts.repository.HobbyRepository;
import com.wejuai.accounts.repository.ImageRepository;
import com.wejuai.accounts.repository.StarRepository;
import com.wejuai.accounts.web.dto.request.CreateArticleDraftRequest;
import com.wejuai.accounts.web.dto.request.CreateArticleRequest;
import com.wejuai.accounts.web.dto.response.StarAndCollection;
import com.wejuai.dto.request.SaveArticleDraftRequest;
import com.wejuai.dto.request.SaveArticleRequest;
import com.wejuai.dto.response.ArticleInfo;
import com.wejuai.dto.response.KeyValue;
import com.wejuai.entity.mongo.Collection;
import com.wejuai.entity.mongo.Star;
import com.wejuai.entity.mysql.Article;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.ImageUploadType;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.wejuai.entity.mongo.AppType.ARTICLE;

/**
 * Created by ZM.Wang
 */
@Service
public class ArticleService {

    private final StarRepository starRepository;
    private final ImageRepository imageRepository;
    private final HobbyRepository hobbyRepository;
    private final ArticleRepository articleRepository;
    private final CollectionRepository collectionRepository;

    private final HobbyService hobbyService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public ArticleService(WejuaiCoreClient wejuaiCoreClient, CollectionRepository collectionRepository, StarRepository starRepository, ImageRepository imageRepository, HobbyRepository hobbyRepository, ArticleRepository articleRepository, HobbyService hobbyService) {
        this.wejuaiCoreClient = wejuaiCoreClient;
        this.collectionRepository = collectionRepository;
        this.starRepository = starRepository;
        this.imageRepository = imageRepository;
        this.hobbyRepository = hobbyRepository;
        this.articleRepository = articleRepository;
        this.hobbyService = hobbyService;
    }

    public void saveArticle(User user, CreateArticleRequest request) {
        Hobby hobby = hobbyService.getHobby(request.getHobbyId());
        hobbyService.checkUserHasHobby(user, hobby);
        SaveArticleRequest templateRequest = new SaveArticleRequest(user.getId(), hobby.getId(),
                request.getTitle(), request.getCoverId(), request.getInShort(), request.getText(), request.getEmailText(), request.getIntegral());
        wejuaiCoreClient.saveArticle(templateRequest);
    }

    public void updateCover(User user, String id, String imageId) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该文章"));
        if (!article.getUser().equals(user)) {
            throw new ForbiddenException("该文章不属于你");
        }
        Image cover = imageRepository.findById(imageId).orElseThrow(() -> new BadRequestException("没有该图片"));
        if (cover.getType() != ImageUploadType.ARTICLE_HEAD) {
            throw new BadRequestException("图片类型错误");
        }
        if (!cover.getUser().equals(user)) {
            throw new BadRequestException("该图片不属于你");
        }
        articleRepository.save(article.setCover(cover));
    }

    public KeyValue saveArticleDraft(User user, CreateArticleDraftRequest request) {
        Hobby hobby = null;
        if (StringUtils.isNotBlank(request.getHobbyId())) {
            hobby = hobbyRepository.findById(request.getHobbyId()).orElse(null);
        }
        if (hobby != null) {
            hobbyService.checkUserHasHobby(user, hobby);
        }
        SaveArticleDraftRequest templateRequest = new SaveArticleDraftRequest(request.getId(), user.getId(), hobby != null ? request.getHobbyId() : null,
                request.getTitle(), request.getCoverId(), request.getInShort(), request.getText(), request.getEmailText(), request.getIntegral());
        return wejuaiCoreClient.saveArticleDraft(templateRequest);
    }

    public StarAndCollection getStarAndCollection(User user, String id) {
        Collection collection = collectionRepository.findByUserIdAndAppTypeAndAppId(user.getId(), ARTICLE, id);
        Star star = starRepository.findByUserIdAndAppTypeAndAppId(user.getId(), ARTICLE, id);
        return new StarAndCollection(star != null, collection != null);
    }

    public ArticleInfo getArticleDraft(User user, String id) {
        ArticleInfo articleInfo = wejuaiCoreClient.getArticleDraft(id);
        if (!StringUtils.equals(user.getId(), articleInfo.getUserId())) {
            throw new BadRequestException("该草稿不属于你");
        }
        return articleInfo;
    }
}

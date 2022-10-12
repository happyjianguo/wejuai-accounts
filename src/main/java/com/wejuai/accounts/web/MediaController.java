package com.wejuai.accounts.web;

import com.endofmaster.commons.aliyun.oss.AliyunOss;
import com.endofmaster.commons.aliyun.oss.UploadCredentials;
import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.config.AliyunConfig;
import com.wejuai.accounts.service.MediaService;
import com.wejuai.accounts.web.dto.request.MatchingMediaRequest;
import com.wejuai.accounts.web.dto.response.MatchingMediaResponse;
import com.wejuai.entity.mysql.Audio;
import com.wejuai.entity.mysql.AudioUploadType;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.ImageUploadType;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.Video;
import com.wejuai.entity.mysql.VideoUploadType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.Valid;
import java.util.Map;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;
import static com.wejuai.entity.mysql.ImageUploadType.SYS_IMAGE;


/**
 * @author ZM.Wang
 */
@Api(tags = "媒体相关")
@RestController
@RequestMapping
public class MediaController {

    private final static Logger logger = LoggerFactory.getLogger(MediaController.class);

    private final static int MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 允许的图片大小
    private final static int MAX_VIDEO_SIZE = 10 * 1024 * 1024; // 允许的视频大小
    private final static int MAX_AUDIO_SIZE = 2 * 1024 * 1024; // 允许的音频大小
    private final static int EXPIRATION = 100; // 上传凭证有效期，单位秒
    private final AliyunOss aliyunOss;
    private final AliyunConfig.OssProperties ossProperties;
    private final MediaService mediaService;

    public MediaController(AliyunOss aliyunOss, AliyunConfig.OssProperties ossProperties, MediaService mediaService) {
        this.aliyunOss = aliyunOss;
        this.ossProperties = ossProperties;
        this.mediaService = mediaService;
    }

    @ApiOperation("获取上传图片凭证")
    @GetMapping("/api/images/credentials/{type}")
    public UploadCredentials getUploadImageCredentials(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable ImageUploadType type) {
        if (type == SYS_IMAGE) {
            throw new BadRequestException("这是不允许的哦~");
        }
        UploadCredentials credentials = aliyunOss.buildUploadCredentials("image/" + type + "/" + user.getId(),
                ossProperties.getImageCallbackUrl(), MAX_IMAGE_SIZE, EXPIRATION);
        logger.debug("阿里云oss上传参数：" + credentials.toString());
        return credentials;
    }

    @ApiOperation("获取上传视频凭证")
    @GetMapping("/api/videos/credentials/{type}")
    public UploadCredentials getUploadVideoCredentials(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable VideoUploadType type) {
        if (type == VideoUploadType.SYSTEM) {
            throw new BadRequestException("这是不允许的哦~");
        }
        UploadCredentials credentials = aliyunOss.buildUploadCredentials("video/" + type + "/" + user.getId(),
                ossProperties.getVideoCallbackUrl(), MAX_VIDEO_SIZE, EXPIRATION);
        logger.debug("阿里云oss上传参数：" + credentials.toString());
        return credentials;
    }

    @ApiOperation("获取上传音频凭证")
    @GetMapping("/api/audios/credentials/{type}")
    public UploadCredentials getUploadAudiosCredentials(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable AudioUploadType type) {
        if (type == AudioUploadType.SYSTEM) {
            throw new BadRequestException("这是不允许的哦~");
        }
        UploadCredentials credentials = aliyunOss.buildUploadCredentials("audio/" + type + "/" + user.getId(),
                ossProperties.getAudioCallbackUrl(), MAX_AUDIO_SIZE, EXPIRATION);
        logger.debug("阿里云oss上传参数：" + credentials.toString());
        return credentials;
    }

    @ApiOperation("补全image信息")
    @PostMapping("/api/images/matching")
    public MatchingMediaResponse matchingImage(@SessionAttribute(SESSION_LOGIN) User user,
                                               @RequestBody @Valid MatchingMediaRequest request) {
        return mediaService.matchingImage(user, request.getOssKey());
    }

    @ApiOperation("补全video信息")
    @PostMapping("/api/videos/matching")
    public MatchingMediaResponse matchingVideo(@SessionAttribute(SESSION_LOGIN) User user,
                                               @RequestBody @Valid MatchingMediaRequest request) {
        return mediaService.matchingVideo(user, request.getOssKey());
    }

    @ApiOperation("补全audio信息")
    @PostMapping("/api/audios/matching")
    public MatchingMediaResponse matchingAudio(@SessionAttribute(SESSION_LOGIN) User user,
                                               @RequestBody @Valid MatchingMediaRequest request) {
        return mediaService.matchingAudio(user, request.getOssKey());
    }

    @ApiOperation(value = "图片上传回调", hidden = true)
    @PostMapping("/images/callback")
    public Map<String, Object> onImageUploaded(String ossKey, Integer size, String mimeType) {
        logger.debug("收到阿里云oss图片上传回调，ossKey={}，size={}，mimeType={}", ossKey, size, mimeType);
        Image image = mediaService.createImage(ossKey);
        return aliyunOss.buildUploadResponse(image.getId(), ossKey, size, mimeType, false);
    }

    @ApiOperation(value = "视频上传回调", hidden = true)
    @PostMapping("/videos/callback")
    public Map<String, Object> onVideoUploaded(String ossKey, Integer size, String mimeType) {
        logger.debug("收到阿里云oss视频上传回调，ossKey={}，size={}，mimeType={}", ossKey, size, mimeType);
        Video video = mediaService.createVideo(ossKey);
        return aliyunOss.buildUploadResponse(video.getId(), ossKey, size, mimeType, false);
    }

    @ApiOperation(value = "音频上传回调", hidden = true)
    @PostMapping("/audios/callback")
    public Map<String, Object> onAudioUploaded(String ossKey, Integer size, String mimeType) {
        logger.debug("收到阿里云oss音频上传回调，ossKey={}，size={}，mimeType={}", ossKey, size, mimeType);
        Audio audio = mediaService.createAudio(ossKey);
        return aliyunOss.buildUploadResponse(audio.getId(), ossKey, size, mimeType, false);
    }

}

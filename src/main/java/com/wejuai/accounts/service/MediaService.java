package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.repository.AudioRepository;
import com.wejuai.accounts.repository.ImageRepository;
import com.wejuai.accounts.repository.VideoRepository;
import com.wejuai.accounts.web.dto.response.MatchingMediaResponse;
import com.wejuai.entity.mysql.Audio;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.Video;
import org.springframework.stereotype.Service;

/**
 * @author ZM.Wang
 */
@Service
public class MediaService {

    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final AudioRepository audioRepository;

    public MediaService(ImageRepository imageRepository, VideoRepository videoRepository, AudioRepository audioRepository) {
        this.imageRepository = imageRepository;
        this.videoRepository = videoRepository;
        this.audioRepository = audioRepository;
    }

    public MatchingMediaResponse matchingImage(User user, String ossKey) {
        Image image = imageRepository.findByOssKey(ossKey);
        if (image == null) {
            throw new BadRequestException("没有该图片上传记录");
        }
        imageRepository.save(image.setUser(user));
        return new MatchingMediaResponse(image);
    }

    public Image createImage(String ossKey) {
        return imageRepository.save(new Image(ossKey, true));
    }

    public MatchingMediaResponse matchingVideo(User user, String ossKey) {
        Video video = videoRepository.findByOssKey(ossKey);
        if (video == null) {
            throw new BadRequestException("没有该视频上传记录");
        }
        videoRepository.save(video.setUser(user));
        return new MatchingMediaResponse(video);
    }

    public Video createVideo(String ossKey) {
        return videoRepository.save(new Video(ossKey));
    }

    public MatchingMediaResponse matchingAudio(User user, String ossKey) {
        Audio audio = audioRepository.findByOssKey(ossKey);
        if (audio == null) {
            throw new BadRequestException("没有该音频上传记录");
        }
        audioRepository.save(audio.setUser(user));
        return new MatchingMediaResponse(audio);
    }

    public Audio createAudio(String ossKey) {
        return audioRepository.save(new Audio(ossKey));
    }
}

package com.wejuai.accounts.web.dto.response;

import com.wejuai.entity.mysql.Audio;
import com.wejuai.entity.mysql.Image;
import com.wejuai.entity.mysql.Video;
import com.wejuai.util.MediaUtils;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZM.Wang
 */
public class MatchingMediaResponse {

    @ApiModelProperty("媒体id")
    private final String id;
    @ApiModelProperty("文件对象的ossKey")
    private final String ossKey;
    @ApiModelProperty("文件地址")
    private final String url;

    public MatchingMediaResponse(Image image) {
        this.id = image.getId();
        this.ossKey = image.getOssKey();
        this.url = MediaUtils.buildUrl(image);
    }

    public MatchingMediaResponse(Video video) {
        this.id = video.getId();
        this.ossKey = video.getOssKey();
        this.url = MediaUtils.buildUrl(video);
    }

    public MatchingMediaResponse(Audio audio) {
        this.id = audio.getId();
        this.ossKey = audio.getOssKey();
        this.url = MediaUtils.buildUrl(audio);
    }

    public String getId() {
        return id;
    }

    public String getOssKey() {
        return ossKey;
    }

    public String getUrl() {
        return url;
    }
}

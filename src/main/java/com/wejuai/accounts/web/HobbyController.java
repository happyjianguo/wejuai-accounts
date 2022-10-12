package com.wejuai.accounts.web;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.service.HobbyService;
import com.wejuai.dto.response.GetHobbyDomain;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "爱好相关")
@RestController
@RequestMapping
public class HobbyController {

    private final HobbyService hobbyService;
    private final WejuaiCoreClient wejuaiCoreClient;

    public HobbyController(HobbyService hobbyService, WejuaiCoreClient wejuaiCoreClient) {
        this.hobbyService = hobbyService;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("关注某爱好")
    @PutMapping("/api/hobby/add/{id}")
    public void followHobby(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.followHobby(user, id);
    }

    @ApiOperation("取消关注某爱好")
    @PutMapping("/api/hobby/reduce/{id}")
    public void unfollowHobby(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id) {
        wejuaiCoreClient.unfollowHobby(user, id);
    }

    @Deprecated
    @ApiOperation(value = "根据关键字获取爱好域名", notes = "如果搜不到或者多个就调腾讯机器人聊天, 已经移入app")
    @GetMapping("/hobby")
    public GetHobbyDomain getHobby(HttpSession session,
                                   @RequestParam String tab) {
        if (StringUtils.isBlank(tab)) {
            throw new BadRequestException("关键字必须填写哦~");
        }
        if (tab.length() > 50) {
            throw new BadRequestException("太~~太长了啦~");
        }
        String watchUserId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isBlank(watchUserId)) {
            watchUserId = session.getId();
        }
        return wejuaiCoreClient.getHobbyDomainByTab(watchUserId, tab);
    }

    @ApiOperation("是否设置某爱好为公开")
    @PutMapping("/api/hobby/{id}/open")
    public void updateHobbyOpen(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable String id, @RequestParam boolean open) {
        hobbyService.updateHobbyOpen(user, id, open);
    }

}

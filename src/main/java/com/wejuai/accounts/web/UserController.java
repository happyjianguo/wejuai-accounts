package com.wejuai.accounts.web;

import com.endofmaster.rest.exception.ServerException;
import com.endofmaster.weixin.basic.WxBasicApi;
import com.endofmaster.weixin.basic.WxOauth2AccessToken;
import com.endofmaster.weixin.qr.code.WxQrCodeApi;
import com.endofmaster.weixin.qr.code.WxQrCodeType;
import com.wejuai.accounts.config.WeixinConfig;
import com.wejuai.accounts.service.AccountsService;
import com.wejuai.accounts.service.HobbyService;
import com.wejuai.accounts.service.OrderService;
import com.wejuai.accounts.service.UserService;
import com.wejuai.accounts.web.dto.request.CreateWithdrawalRequest;
import com.wejuai.accounts.web.dto.request.EditEmailRequest;
import com.wejuai.accounts.web.dto.request.UpdateUserInfoRequest;
import com.wejuai.accounts.web.dto.request.UpdateWxPhoneRequest;
import com.wejuai.accounts.web.dto.request.UpdateWxUserInfoRequest;
import com.wejuai.accounts.web.dto.response.AccountInfo;
import com.wejuai.accounts.web.dto.response.OtherAccountsInfo;
import com.wejuai.accounts.web.dto.response.TimInfoResponse;
import com.wejuai.accounts.web.dto.response.UserDetailedInfo;
import com.wejuai.accounts.web.dto.response.UserMsgInfo;
import com.wejuai.accounts.web.dto.response.UserNoticeNumInfo;
import com.wejuai.dto.request.RechargeRequest;
import com.wejuai.dto.response.HobbyInfo;
import com.wejuai.dto.response.UserBaseInfo;
import com.wejuai.dto.response.UserInfo;
import com.wejuai.entity.mysql.OauthType;
import com.wejuai.entity.mysql.Performance;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.WeixinUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;
import static com.wejuai.accounts.config.SecurityConfig.WX_SESSION_KEY;

/**
 * @author ZM.Wang
 */
@Api(tags = "用户相关")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final AccountsService accountsService;
    private final OrderService orderService;
    private final HobbyService hobbyService;

    private final WxQrCodeApi wxQrCodeApi;
    private final WxBasicApi wxAppBasicApi;
    private final WeixinConfig weixinConfig;

    public UserController(UserService userService, AccountsService accountsService, OrderService orderService, HobbyService hobbyService, WxQrCodeApi wxQrCodeApi, WxBasicApi wxAppBasicApi, WeixinConfig weixinConfig) {
        this.userService = userService;
        this.accountsService = accountsService;
        this.orderService = orderService;
        this.hobbyService = hobbyService;
        this.wxQrCodeApi = wxQrCodeApi;
        this.wxAppBasicApi = wxAppBasicApi;
        this.weixinConfig = weixinConfig;
    }

    @ApiOperation("获取用户基础信息")
    @GetMapping
    public UserInfo getUserInfo(@SessionAttribute(SESSION_LOGIN) User user) {
        return new UserInfo(user, true);
    }

    @ApiOperation("获取用户第三方帐号列表")
    @GetMapping("/otherAccountsInfo")
    public List<OtherAccountsInfo> getOtherAccountsInfo(@SessionAttribute(SESSION_LOGIN) User user) {
        return userService.getOtherAccountsInfo(user);
    }

    @ApiOperation("解除绑定第三方账号")
    @PutMapping("/unBind")
    public void unBindOtherUser(@SessionAttribute(SESSION_LOGIN) User user,
                                @RequestParam OauthType type) {
        userService.unBindOtherUser(user, type);
    }

    @ApiOperation("获取用户详细信息")
    @GetMapping("/userDetailedInfo")
    public UserDetailedInfo getUserDetailedInfo(@SessionAttribute(SESSION_LOGIN) User user) {
        return userService.getUserDetailedInfo(user);
    }

    @ApiOperation("更新用户基础信息")
    @PutMapping
    public void updateUserInfo(@SessionAttribute(SESSION_LOGIN) User user,
                               @RequestBody @Valid UpdateUserInfoRequest request) {
        userService.updateInfo(user, request);
    }

    @ApiOperation("更新用户头像")
    @PutMapping("/headImage/{imageId}")
    public void updateHeadImage(@SessionAttribute(SESSION_LOGIN) User user,
                                @PathVariable String imageId) throws InterruptedException {
        userService.updateHeadImage(user, imageId);
    }

    @ApiOperation("更新用户封面")
    @PutMapping("/cover/{imageId}")
    public void updateCover(@SessionAttribute(SESSION_LOGIN) User user,
                            @PathVariable String imageId) throws InterruptedException {
        userService.updateCover(user, imageId);
    }

    @ApiOperation("关注某用户,followUserId：要关注的User的id")
    @PostMapping("/follow/{followUserId}")
    public void follow(@SessionAttribute(SESSION_LOGIN) User user,
                       @PathVariable String followUserId) {
        userService.follow(user, followUserId);
    }

    @ApiOperation("取消关注某用户,followUserId：要取消关注的User的id")
    @PostMapping("/unFollow/{followUserId}")
    public void unFollow(@SessionAttribute(SESSION_LOGIN) User user,
                         @PathVariable String followUserId) {
        userService.unFollow(user, followUserId);
    }

    @ApiOperation("获取用户关注列表")
    @GetMapping("/attention")
    public Page<UserBaseInfo> attentionPage(@SessionAttribute(SESSION_LOGIN) User user,
                                            @RequestParam(required = false, defaultValue = "") String titleStr,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return userService.attentionPage(user, titleStr, pageable);
    }

    @ApiOperation("获取用户粉丝列表")
    @GetMapping("/follow")
    public Page<UserBaseInfo> followPage(@SessionAttribute(SESSION_LOGIN) User user,
                                         @RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return userService.followPage(user, pageable);
    }

    @ApiOperation("获取设置中的帐号信息")
    @GetMapping("/accounts")
    public AccountInfo getAccountInfo(@SessionAttribute(SESSION_LOGIN) User user) {
        return accountsService.getAccountInfo(user);
    }

    @ApiOperation("修改邮箱发送旧邮箱验证码")
    @GetMapping("/email/old")
    public void getEditEmailOldEmailCode(@SessionAttribute(SESSION_LOGIN) User user) {
        accountsService.editEmailSendOldMail(user);
    }

    @ApiOperation("修改邮箱发送新邮箱验证码")
    @GetMapping("/email/new")
    public void getEditEmailNewEmailCode(@SessionAttribute(SESSION_LOGIN) User user,
                                         @ApiParam("新邮箱") @RequestParam String newEmail) {
        accountsService.editEmailSendNewMail(newEmail, user);
    }

    @ApiOperation("修改邮箱")
    @PutMapping("/email")
    public void editEmail(@SessionAttribute(SESSION_LOGIN) User user,
                          @RequestBody @Valid EditEmailRequest request) {
        accountsService.editEmail(user, request.getNewEmail(), request.getNewEmailCode(), request.getOldEmailCode());
    }

    @ApiOperation("获取聊天插件参数")
    @GetMapping("/tim")
    public TimInfoResponse getTimInfo(@SessionAttribute(SESSION_LOGIN) User user) {
        return new TimInfoResponse(System.currentTimeMillis() / 1000, user.getAccounts().getId());
    }

    @ApiOperation("充值")
    @PostMapping("/recharge")
    public Map<String, String> recharge(@SessionAttribute(SESSION_LOGIN) User user,
                                        @RequestBody @Valid RechargeRequest request,
                                        HttpServletRequest servletRequest) {
        String ip = servletRequest.getRemoteAddr();
        return orderService.recharge(user, request, ip);
    }

    @ApiOperation("提现申请")
    @PostMapping("/withdrawal")
    public void withdrawal(@SessionAttribute(SESSION_LOGIN) User user, @RequestBody @Valid CreateWithdrawalRequest request) {
        orderService.withdrawal(user, request);
    }

    @ApiModelProperty("是否有绑定微信提现参数")
    @GetMapping("/hasWxWithdrawalOpenId")
    public Map<String, Object> hasWxWithdrawalOpenId(@SessionAttribute(SESSION_LOGIN) User user) {
        boolean has = false;
        WeixinUser weixinUser = user.getAccounts().getWeixinUser();
        if (weixinUser != null && (StringUtils.isNotBlank(weixinUser.getAppOpenId()) || StringUtils.isNotBlank(weixinUser.getAppOpenId()))) {
            has = true;
        }
        return Collections.singletonMap("has", has);
    }

    // todo 对于初次绑定微信的用户只能创建weixinUser无法关联到user
    @ApiOperation("获取绑定微信提现参数二维码")
    @GetMapping("/getWxQrCodeUrl")
    public Map<String, String> getWxQrCodeUrl() {
        String accessToken = accountsService.getAccessToken();
        String url = wxQrCodeApi.getTemporaryQrCodeUrl(100, accessToken, WxQrCodeType.TEMPORARY);
        if (StringUtils.isBlank(url)) {
            throw new ServerException("服务器错误");
        }
        return Collections.singletonMap("url", url);
    }

    @Deprecated
    @ApiOperation("用户通知数量信息，准备删除")
    @GetMapping("/userMsgNum")
    public UserNoticeNumInfo getUserNoticeNumInfo(@SessionAttribute(SESSION_LOGIN) User user) {
        return userService.getUserNoticeNumInfo(user);
    }

    @ApiOperation("用户通知数量信息")
    @GetMapping("/noticeNum")
    public UserNoticeNumInfo getNoticeNum(@SessionAttribute(SESSION_LOGIN) User user) {
        return userService.getUserNoticeNumInfo(user);
    }

    @ApiOperation("用户消息数量信息")
    @GetMapping("/msgNum")
    public UserMsgInfo getUserMsgInfo(@SessionAttribute(SESSION_LOGIN) User user) {
        return userService.getUserMsgInfo(user);
    }

    @ApiOperation("更新微信用户信息")
    @PutMapping("/wxUserInfo")
    public void updateWxUserInfo(@SessionAttribute(SESSION_LOGIN) User user, @RequestBody UpdateWxUserInfoRequest request) {
        userService.updateWxUserInfo(user, request);
    }

    @ApiOperation("更新微信小程序用户手机号")
    @PutMapping("/phone/wxMini")
    public void updateWxPhone(@SessionAttribute(SESSION_LOGIN) User user, @SessionAttribute(WX_SESSION_KEY) String sessionKey,
                              @RequestBody @Valid UpdateWxPhoneRequest request, HttpSession session) {
        if (StringUtils.isNotBlank(request.getCode())) {
            WxOauth2AccessToken oauth2AccessToken = wxAppBasicApi.getOpenIdBySmallProgram(request.getCode(), weixinConfig.getAppSecret());
            sessionKey = oauth2AccessToken.getSessionKey();
            session.setAttribute(WX_SESSION_KEY, sessionKey);
        }
        logger.debug("更新手机号获取的sessionKey: " + sessionKey);
        userService.updateWxPhone(user, request, sessionKey);
    }

    @ApiOperation("用户自己的爱好列表")
    @GetMapping("/hobbies")
    public List<HobbyInfo> getUserHobbies(@SessionAttribute(SESSION_LOGIN) User user) {
        return hobbyService.getUserHobbies(user);
    }

    @ApiOperation("修改用户手机性能参数")
    @PutMapping("/performance/{performance}")
    public void updatePerformance(@SessionAttribute(SESSION_LOGIN) User user, @PathVariable Performance performance) {
        userService.updatePerformance(user, performance);
    }

    @ApiOperation("申请注销账号")
    @PostMapping("/cancelAccount")
    public void applyCancelAccount(@SessionAttribute(SESSION_LOGIN) User user,
                                   @RequestParam(required = false, defaultValue = "") String reason) {
        userService.applyCancelAccount(user, reason);
    }

}

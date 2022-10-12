package com.wejuai.accounts.web;

import com.endofmaster.qq.basic.QqBasicApi;
import com.endofmaster.qq.basic.QqOauth2AccessToken;
import com.endofmaster.qq.basic.QqOpenId;
import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ServerException;
import com.endofmaster.weibo.basic.WeiboBasicApi;
import com.endofmaster.weibo.basic.WeiboOauth2AccessToken;
import com.endofmaster.weixin.basic.WxAuthUserInfo;
import com.endofmaster.weixin.basic.WxBasicApi;
import com.endofmaster.weixin.basic.WxOauth2AccessToken;
import com.endofmaster.weixin.support.WxDecryptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.wejuai.accounts.config.WeixinConfig;
import com.wejuai.accounts.service.AccountsService;
import com.wejuai.accounts.service.OrderService;
import com.wejuai.accounts.service.UserService;
import com.wejuai.accounts.service.dto.WxSessionInfo;
import com.wejuai.accounts.web.dto.request.CreateEmailCodeRequest;
import com.wejuai.accounts.web.dto.request.LoginRequest;
import com.wejuai.accounts.web.dto.request.SignUpRequest;
import com.wejuai.accounts.web.dto.request.VerifyEmailCodeRequest;
import com.wejuai.accounts.web.dto.request.WxUserInfoRequest;
import com.wejuai.accounts.web.dto.response.WxMiniAppLoginInfo;
import com.wejuai.entity.mysql.Accounts;
import com.wejuai.entity.mysql.Sex;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.WeixinUser;
import com.wejuai.entity.session.BindRequest;
import com.wejuai.entity.session.WeixinUserInfo;
import com.wejuai.entity.session.WxLoginType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import springfox.documentation.annotations.ApiIgnore;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wejuai.accounts.config.Constant.MAPPER;
import static com.wejuai.accounts.config.SecurityConfig.BIND_REQUEST;
import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;
import static com.wejuai.accounts.config.SecurityConfig.WX_SESSION_INFO;
import static com.wejuai.accounts.config.SecurityConfig.WX_SESSION_KEY;
import static org.apache.catalina.manager.Constants.CHARSET;

/**
 * @author ZM.Wang
 */
@Api(tags = "帐号相关")
@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final static Logger logger = LoggerFactory.getLogger(AccountsController.class);

    private final AccountsService accountsService;
    private final QqBasicApi qqBasicApi;
    private final WeiboBasicApi weiboBasicApi;
    private final UserService userService;
    private final WxBasicApi wxOpenBasicApi;
    private final WxBasicApi wxOffiBasicApi;
    private final WxBasicApi wxAppBasicApi;
    private final WeixinConfig weixinConfig;
    private final OrderService orderService;

    public AccountsController(AccountsService accountsService, QqBasicApi qqBasicApi, WeiboBasicApi weiboBasicApi, UserService userService, WxBasicApi wxOpenBasicApi, WxBasicApi wxOffiBasicApi, WxBasicApi wxAppBasicApi, WeixinConfig weixinConfig, OrderService orderService) {
        this.accountsService = accountsService;
        this.qqBasicApi = qqBasicApi;
        this.weiboBasicApi = weiboBasicApi;
        this.userService = userService;
        this.wxOpenBasicApi = wxOpenBasicApi;
        this.wxOffiBasicApi = wxOffiBasicApi;
        this.wxAppBasicApi = wxAppBasicApi;
        this.weixinConfig = weixinConfig;
        this.orderService = orderService;
    }

    /**
     * 1.用户点击按钮，前端直接跳转进入该接口，带上当前页面连接参数跳转到qq页面
     *
     * @param state 前段进入这个接口前的链接，用于操作结束后回到用户最开始的页面
     */
    @ApiOperation("通过QQ登录,前端直接跳转进入该接口，state是进入时的链接地址")
    @GetMapping("/login/qq")
    public void loginWithQq(HttpServletResponse servletResponse, @RequestParam(required = false, defaultValue = "") String state) throws IOException {
        String url = qqBasicApi.getBaseAuthorizeUrl(state);
        servletResponse.sendRedirect(url);
    }

    @ApiOperation("通过微博登录,详情和qq一样")
    @GetMapping("/login/weibo")
    public void loginWithWeibo(HttpServletResponse servletResponse, @RequestParam(required = false, defaultValue = "") String state) throws IOException {
        String url = weiboBasicApi.getBaseAuthorizeUrl(state);
        servletResponse.sendRedirect(url);
    }

    @ApiOperation("通过微信登录,详情和qq一样")
    @GetMapping("/login/wx")
    public void loginWithWeixin(HttpServletResponse servletResponse,
                                @RequestParam(required = false, defaultValue = "") String state,
                                @RequestParam @NotNull WxLoginType type) throws IOException {
        String url;
        logger.debug("微信登录链接获取到的type：" + type);
        if (type == WxLoginType.PC) {
            url = wxOpenBasicApi.getLoginAuthorizeUrl(weixinConfig.getRedirectUrl() + "/" + type, state);
        } else if (type == WxLoginType.OFFIACCOUNT || type == WxLoginType.MINIPROGRAM) {//小程序是用于在小程序内拉起公众号登录
            url = wxOffiBasicApi.getUserAuthorizeUrl(weixinConfig.getRedirectUrl() + "/" + type, state);
        } else {
            logger.error("微信登录类型错误：" + type);
            return;
        }
        servletResponse.sendRedirect(url);
    }

    /**
     * 2.qq页面用户授权后跳入该接口，查询帐号，如果存在登录成功，并且跳转到之前点击登录的页面，不存在就跳转到注册页，并且在session中存储qq信息
     *
     * @param code  qq给的用来换信息的code
     * @param state 之前的链接
     */
    @Transactional
    @ApiOperation(value = "", hidden = true)
    @GetMapping("/authorize/qq")
    public void onQqAuthorized(@RequestParam String code,
                               @RequestParam(required = false, defaultValue = "") String state,
                               @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                               HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String ip = (ips == null || ips.size() < 1) ? request.getRemoteHost() : ips.get(0);
        logger.debug("QQ登录获取的ip：" + ip);
        if (StringUtils.isBlank(state)) {
            state = "https://www.wejuai.com";
        }
        QqOauth2AccessToken oauth2AccessToken = qqBasicApi.getOauth2AccessToken(code);
        logger.debug("qq第一步请求完成");
        QqOpenId qqOpenId = qqBasicApi.getOauth2OpenId(oauth2AccessToken.getAccessToken());
        String openId = qqOpenId.getOpenId();
        logger.debug("QQ授权回调获取openId：" + openId);
        //session存在user就是在重新绑定qq
        String userId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.getUser(userId);
            accountsService.bindQq(openId, oauth2AccessToken.getAccessToken(), user.getAccounts());
            response.sendRedirect(state);
            return;
        }
        try {
            //不存在就是快速登录
            Accounts accounts = accountsService.getAccountsByQq(openId);//这个找不到账号会抛出异常
            accountsService.loginLog(accounts, ip);
            User user = accountsService.getUserByUnionId(accounts);
            if (user.getBan()) {
                response.sendRedirect("/ban.html");
                return;
            }
            session.setAttribute(SESSION_LOGIN, user.getId());
            response.sendRedirect(state);
        } catch (BadRequestException e) {
            //第一次登录并且没注册
            session.setAttribute(BIND_REQUEST, BindRequest.qq(oauth2AccessToken.getAccessToken(), openId));
            response.sendRedirect("/signUp.html?type=other&state=" + URLEncoder.encode(state, CHARSET));
        }
    }

    @Transactional
    @ApiOperation(value = "", hidden = true)
    @GetMapping("/authorize/weibo")
    public void onWeiboAuthorized(@RequestParam String code,
                                  @RequestParam(required = false, defaultValue = "") String state,
                                  @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                                  HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String ip = (ips == null || ips.size() < 1) ? request.getRemoteHost() : ips.get(0);
        logger.debug("weibo登录获取的ip：" + ip);
        if (StringUtils.isBlank(state)) {
            state = "https://www.wejuai.com";
        }
        WeiboOauth2AccessToken oauth2AccessToken = weiboBasicApi.getOauth2AccessToken(code);
        String uid = oauth2AccessToken.getUid() + "";
        logger.debug("微博授权回调获取uid：" + uid);

        String userId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.getUser(userId);
            accountsService.bindWeibo(uid, oauth2AccessToken.getAccessToken(), user.getAccounts());
            response.sendRedirect(state);
            return;
        }
        try {
            Accounts accounts = accountsService.getAccountsByWeibo(uid);
            accountsService.loginLog(accounts, ip);
            User user = accountsService.getUserByUnionId(accounts);
            if (user.getBan()) {
                response.sendRedirect("/ban.html");
                return;
            }
            session.setAttribute(SESSION_LOGIN, user.getId());
            response.sendRedirect(state);
        } catch (BadRequestException e) {
            session.setAttribute(BIND_REQUEST, BindRequest.weibo(oauth2AccessToken.getAccessToken(), uid));
            response.sendRedirect("/signUp.html?type=other&state=" + URLEncoder.encode(state, CHARSET));
        }
    }

    @Transactional
    @ApiOperation(value = "微信登录回调", hidden = true)
    @GetMapping("/authorize/wx/{type}")
    public void onWeixinAuthorized(@PathVariable WxLoginType type,
                                   @RequestParam String code,
                                   @RequestParam(required = false, defaultValue = "") String state,
                                   @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                                   HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String ip = (ips == null || ips.size() < 1) ? request.getRemoteHost() : ips.get(0);
        logger.debug("weixin登录获取的ip：" + ip);
        if (StringUtils.isBlank(state)) {
            state = "https://www.wejuai.com";
        }
        WeixinUserInfo weixinUserInfo;
        if (type == WxLoginType.PC) {
            WxOauth2AccessToken oauth2AccessToken = wxOpenBasicApi.getOauth2AccessToken(code, weixinConfig.getOpenSecret());
            WxAuthUserInfo oauth2UserInfo = wxOpenBasicApi.getOauth2UserInfo(oauth2AccessToken.getOpenId(), oauth2AccessToken.getAccessToken());
            weixinUserInfo = new WeixinUserInfo(oauth2UserInfo.getProvince(), oauth2UserInfo.getCity(), oauth2UserInfo.getCountry(),
                    Sex.find(oauth2UserInfo.getSex()), oauth2UserInfo.getHeadImgUrl(), oauth2UserInfo.getNickName(),
                    oauth2UserInfo.getUnionId(), oauth2UserInfo.getOpenId(), WxLoginType.PC);
        } else if (type == WxLoginType.OFFIACCOUNT || type == WxLoginType.MINIPROGRAM) {
            WxOauth2AccessToken oauth2AccessToken = wxOffiBasicApi.getOauth2AccessToken(code, weixinConfig.getOffiSecret());
            WxAuthUserInfo wxAuthUserInfo = wxOffiBasicApi.getOauth2UserInfo(oauth2AccessToken.getOpenId(), oauth2AccessToken.getAccessToken());
            weixinUserInfo = new WeixinUserInfo(wxAuthUserInfo.getProvince(), wxAuthUserInfo.getCity(), wxAuthUserInfo.getCountry(),
                    Sex.find(wxAuthUserInfo.getSex()), wxAuthUserInfo.getHeadImgUrl(), wxAuthUserInfo.getNickName(),
                    wxAuthUserInfo.getUnionId(), wxAuthUserInfo.getOpenId(), WxLoginType.OFFIACCOUNT);
        } else {
            logger.error("微信登录类型错误：" + type);
            return;
        }
        if (type == WxLoginType.MINIPROGRAM) {
            accountsService.saveWeixinUser(weixinUserInfo);
            if (StringUtils.isNotBlank(state)) {
                response.sendRedirect(state);
            }
            return;
        }

        String userId = (String) session.getAttribute(SESSION_LOGIN);
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.getUser(userId);
            accountsService.bindWeixin(weixinUserInfo, user.getAccounts());
            response.sendRedirect(state);
            return;
        }
        try {
            //根据unionId查找账号和user，找不到会跳转到注册或者绑定
            Accounts accounts = accountsService.getAccountsByWeixin(weixinUserInfo.getUnionId());
            accountsService.loginLog(accounts, ip);
            User user = accountsService.getUserByUnionId(accounts);
            if (user.getBan()) {
                response.sendRedirect("/ban.html");
                return;
            }
            session.setAttribute(SESSION_LOGIN, user.getId());
            response.sendRedirect(state);
        } catch (BadRequestException e) {
            session.setAttribute(BIND_REQUEST, BindRequest.weixin(weixinUserInfo));
            response.sendRedirect("/signUp.html?type=other&state=" + URLEncoder.encode(state, CHARSET));
        }
    }

    /**
     * 3.采用邮箱验证码方式，获取验证码
     *
     * @param request 邮箱地址
     */
    @ApiOperation("发送注册邮箱验证码")
    @PostMapping("/email")
    public void createEmailCode(@RequestBody @Valid CreateEmailCodeRequest request) {
        accountsService.createEmailCodeSignUp(request);
    }

    //4.从session中获取第三方登录信息，如果不存在就是普通邮箱注册，如果存在判断第三方类型然后调用不同的注册

    /**
     * 先行注册新的账号和用户信息，如果查询到email已存在会抛出异常，注册新的账号后把session中存储的第三方信息绑定
     *
     * @param request 邮箱信息
     */
    @Transactional
    @ApiOperation("注册新账号")
    @PostMapping("/signUp")
    public void signUp(@RequestBody @Valid SignUpRequest request, HttpSession session,
                       @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                       HttpServletRequest servletRequest) {
        String ip = (ips == null || ips.size() < 1) ? servletRequest.getRemoteHost() : ips.get(0);
        User user = accountsService.signUp(request);
        if (user.getBan()) {
            throw new BadRequestException("该账号已被封禁");
        }
        BindRequest bind = (BindRequest) session.getAttribute(BIND_REQUEST);
        if (bind == null) {
            session.setAttribute(SESSION_LOGIN, user.getId());
            accountsService.loginLog(user.getAccounts(), ip);
            return;
        }
        logger.debug("从session中获取的第三方帐号信息：" + bind);
        bind(session, bind, user);
        accountsService.loginLog(user.getAccounts(), ip);
    }

    /**
     * 在第三方页面进入后选择已有邮箱账号，登录进入会验证密码，从session中获取第三方账号绑定到这个账号上，会验证第三方是否绑定到其他账号上，并顶掉
     *
     * @param request 邮箱和密码
     */
    @Transactional
    @ApiOperation("绑定第三方帐号")
    @PostMapping("/bind")
    public void bind(@RequestBody @Valid LoginRequest request, HttpSession session,
                     @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                     HttpServletRequest servletRequest) {
        String ip = (ips == null || ips.size() < 1) ? servletRequest.getRemoteHost() : ips.get(0);
        BindRequest bind = (BindRequest) session.getAttribute(BIND_REQUEST);
        if (bind == null) {
            throw new BadRequestException("未获取到第三方授权");
        }
        Accounts accounts = accountsService.findByEmail(request);
        User user = accountsService.getUserByUnionId(accounts);
        if (user.getBan()) {
            throw new BadRequestException("该账号已被封禁");
        }
        bind(session, bind, user);
        accountsService.loginLog(accounts, ip);
    }

    @ApiOperation("小程序注册")
    @PostMapping("/signUp/wx")
    public void weixinMiniProgramSignup(@RequestBody @Valid WxUserInfoRequest request,
                                        @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                                        @SessionAttribute(WX_SESSION_INFO) WxSessionInfo wxSessionInfo,
                                        HttpServletRequest servletRequest, HttpSession session) {
        String ip = (ips == null || ips.size() < 1) ? servletRequest.getRemoteHost() : ips.get(0);
        logger.debug("小程序注册获取的ip：" + ip);
        try {
            //解密微信加密信息
            String dataJson = WxDecryptionUtils.decryption(request.getEncryptedData(), request.getIv(), wxSessionInfo.getSessionKey());
            logger.debug("微信小程序登录加密信息解密结果：" + dataJson);
            JsonNode jsonNode = MAPPER.readTree(dataJson);
            String unionId = jsonNode.get("unionId").asText();
            //组装weixin信息
            WeixinUserInfo weixinUserInfo = new WeixinUserInfo(request.getProvince(), request.getCity(), request.getCountry(),
                    Sex.find(request.getGender()), request.getAvatarUrl(), request.getNickName(), unionId, wxSessionInfo.getOpenId(), WxLoginType.MINIPROGRAM);
            User user = accountsService.signupByWx(weixinUserInfo, ip);
            if (user.getBan()) {
                throw new BadRequestException("该账号已被封禁");
            }
            session.setAttribute(SESSION_LOGIN, user.getId());
            session.setAttribute(WX_SESSION_KEY, wxSessionInfo.getSessionKey());
            session.removeAttribute(WX_SESSION_INFO);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | JsonProcessingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            throw new ServerException("解密微信信息错误", e);
        }
    }

    @ApiOperation("小程序登录")
    @PostMapping("/login/wx")
    public WxMiniAppLoginInfo weixinMiniAppLogin(@RequestParam String code,
                                                 @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                                                 HttpServletRequest servletRequest, HttpSession session) {
        String ip = (ips == null || ips.size() < 1) ? servletRequest.getRemoteHost() : ips.get(0);
        logger.debug("小程序登录获取的ip：" + ip);
        WxOauth2AccessToken oauth2AccessToken = wxAppBasicApi.getOpenIdBySmallProgram(code, weixinConfig.getAppSecret());
        String openId = oauth2AccessToken.getOpenId();
        WeixinUser weixinUser = accountsService.getWeixinUserByAppOpenId(openId);
        String unionId = oauth2AccessToken.getUnionId();
        if (StringUtils.isBlank(unionId) && weixinUser != null) {
            unionId = weixinUser.getUnionId();
        }
        if (StringUtils.isBlank(unionId)) {
            session.setAttribute(WX_SESSION_INFO, new WxSessionInfo(oauth2AccessToken.getSessionKey(), openId));
            return new WxMiniAppLoginInfo(false);
        }
        User user = accountsService.loginByWx(new WeixinUserInfo(unionId, openId, WxLoginType.MINIPROGRAM), ip);
        if (user.getBan()) {
            throw new BadRequestException("该账号已被封禁");
        }
        session.setAttribute(SESSION_LOGIN, user.getId());
        session.setAttribute(WX_SESSION_KEY, oauth2AccessToken.getSessionKey());
        return new WxMiniAppLoginInfo(true);
    }

    @ApiOperation("发送修改密码邮件")
    @PostMapping("/passwordReset")
    public void passwordReset(@RequestBody @Valid CreateEmailCodeRequest request) {
        accountsService.passwordResetSendMail(request);
    }

    @ApiOperation("验证修改密码验证码")
    @PostMapping("/passwordReset/verify")
    public void passwordResetVerify(@RequestBody @Valid VerifyEmailCodeRequest request, HttpSession session) {
        accountsService.passwordResetVerifyEmailCode(request, session);
    }

    @ApiOperation("普通邮箱登录")
    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequest request,
                      @RequestHeader(value = "x-real-ip", required = false) List<String> ips,
                      HttpServletRequest servletRequest, HttpSession session) {
        String ip = (ips == null || ips.size() < 1) ? servletRequest.getRemoteHost() : ips.get(0);
        logger.debug("普通登录获得ip:" + ip);
        User user = accountsService.login(request, ip);
        if (user.getBan()) {
            throw new BadRequestException("该账号已被封禁");
        }
        session.setAttribute(SESSION_LOGIN, user.getId());
    }

    @ApiOperation("登出")
    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_LOGIN);
    }

    @ApiIgnore
    @GetMapping("/let/{money}")
    public void privatePayment(@PathVariable long money, HttpServletResponse response) throws IOException {
        orderService.privatePayment(money, response);
    }

    @GetMapping("/test")
    public void originTest(HttpServletRequest request) {
        logger.info("项目请求来源测试");
        logger.info("RemoteAddr：" + request.getRemoteAddr());
        logger.info("LocalAddr：" + request.getLocalAddr());
        logger.info("ContextPath：" + request.getContextPath());
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement();
            logger.info("key：{}，value：{}", key, request.getHeader(key));
        }
    }

    @ApiOperation("检查登录状态")
    @GetMapping("/checkLoginStatus")
    public Map<String, Boolean> checkLoginStatus(@SessionAttribute(required = false, value = SESSION_LOGIN) String user) {
        return new HashMap<>(Collections.singletonMap("login", StringUtils.isNotBlank(user)));
    }

    private void bind(HttpSession session, BindRequest bind, User user) {
        switch (bind.getOauthType()) {
            case QQ:
                accountsService.bindQq(bind.getOpenId(), bind.getAccessToken(), user.getAccounts());
                break;
            case WEIBO:
                accountsService.bindWeibo(bind.getOpenId(), bind.getAccessToken(), user.getAccounts());
                break;
            case WEIXIN:
                accountsService.bindWeixin(bind.getWeixinUserInfo(), user.getAccounts());
                break;
            default:
                logger.warn("不知道什么鬼的还没开通的type出现了：" + bind);
                throw new BadRequestException("不知道什么鬼的错误");
        }
        session.removeAttribute(BIND_REQUEST);
        session.setAttribute(SESSION_LOGIN, user.getId());
    }
}

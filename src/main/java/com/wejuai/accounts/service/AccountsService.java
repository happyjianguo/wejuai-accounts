package com.wejuai.accounts.service;

import com.endofmaster.commons.util.DateUtil;
import com.endofmaster.qq.basic.QqAuthUserInfo;
import com.endofmaster.qq.basic.QqBasicApi;
import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.NotFoundException;
import com.endofmaster.rest.exception.ServerException;
import com.endofmaster.weibo.basic.WeiboAuthUserInfo;
import com.endofmaster.weibo.basic.WeiboBasicApi;
import com.wejuai.accounts.config.SecurityConfig;
import com.wejuai.accounts.config.WeixinConfig;
import com.wejuai.accounts.domain.EmailCode;
import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.infrastructure.mail.MailPoster;
import com.wejuai.accounts.infrastructure.verificationCode.VerificationCodeGenerator;
import com.wejuai.accounts.repository.*;
import com.wejuai.accounts.web.dto.request.CreateEmailCodeRequest;
import com.wejuai.accounts.web.dto.request.LoginRequest;
import com.wejuai.accounts.web.dto.request.SignUpRequest;
import com.wejuai.accounts.web.dto.request.VerifyEmailCodeRequest;
import com.wejuai.accounts.web.dto.response.AccountInfo;
import com.wejuai.dto.request.SaveCelestialBodyRequest;
import com.wejuai.entity.mongo.LoginLog;
import com.wejuai.entity.mongo.WeiXinToken;
import com.wejuai.entity.mysql.*;
import com.wejuai.entity.session.WeixinUserInfo;
import com.wejuai.entity.session.WxLoginType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.wejuai.entity.mongo.CelestialBodyType.UNOWNED;
import static com.wejuai.entity.mongo.CelestialBodyType.USER;

/**
 * @author ZM.Wang
 */
@Service
public class AccountsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final QqUserRepository qqUserRepository;
    private final AccountsRepository accountsRepository;
    private final LoginLogRepository loginLogRepository;
    private final EmailCodeRepository emailCodeRepository;
    private final WeiboUserRepository weiboUserRepository;
    private final WeixinUserRepository weixinUserRepository;
    private final WeiXinTokenRepository weiXinTokenRepository;

    private final MailPoster mailPoster;
    private final QqBasicApi qqBasicApi;
    private final WeixinConfig weixinConfig;
    private final WeiboBasicApi weiboBasicApi;
    private final WejuaiCoreClient wejuaiCoreClient;
    private final VerificationCodeGenerator verificationCodeGenerator;

    public AccountsService(EmailCodeRepository emailCodeRepository, AccountsRepository accountsRepository, UserRepository userRepository, QqUserRepository qqUserRepository, WeiboUserRepository weiboUserRepository, WeixinUserRepository weixinUserRepository, LoginLogRepository loginLogRepository, WeiXinTokenRepository weiXinTokenRepository, MailPoster mailPoster, VerificationCodeGenerator verificationCodeGenerator, QqBasicApi qqBasicApi, WeiboBasicApi weiboBasicApi, WeixinConfig weixinConfig, WejuaiCoreClient wejuaiCoreClient) {
        this.emailCodeRepository = emailCodeRepository;
        this.accountsRepository = accountsRepository;
        this.userRepository = userRepository;
        this.qqUserRepository = qqUserRepository;
        this.weiboUserRepository = weiboUserRepository;
        this.weixinUserRepository = weixinUserRepository;
        this.loginLogRepository = loginLogRepository;
        this.weiXinTokenRepository = weiXinTokenRepository;
        this.mailPoster = mailPoster;
        this.verificationCodeGenerator = verificationCodeGenerator;
        this.qqBasicApi = qqBasicApi;
        this.weiboBasicApi = weiboBasicApi;
        this.weixinConfig = weixinConfig;
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @Transactional
    public User signUp(SignUpRequest request) {
        String email = request.getEmail();
        Accounts accounts = accountsRepository.findByEmail(email);
        if (accounts != null) {
            throw new BadRequestException("该邮箱已经注册了哦~");
        }
        verifyEmailCode(email, request.getCode());
        accounts = accountsRepository.save(new Accounts(email, request.getPassword()));
        return userRepository.save(new User(accounts).updateNickName(email));
    }

    @Transactional
    public User loginByWx(WeixinUserInfo weixinUserInfo, String ip) {
        WeixinUser weixinUser = weixinUserRepository.findByUnionId(weixinUserInfo.getUnionId());
        if (weixinUser == null) {
            weixinUser = new WeixinUser();
        }
        weixinUser = weixinUserRepository.save(weixinUser.update(weixinUserInfo.getOpenId(), weixinUserInfo.getUnionId()));
        return getUserByUnionId(weixinUserInfo, ip, weixinUser);
    }

    @Transactional
    public User signupByWx(WeixinUserInfo weixinUserInfo, String ip) {
        WeixinUser weixinUser = weixinUserRepository.findByUnionId(weixinUserInfo.getUnionId());
        if (weixinUser == null) {
            weixinUser = getWeixinUserByAppOpenId(weixinUserInfo.getOpenId());
        }
        if (weixinUser == null) {
            weixinUser = new WeixinUser();
        }
        weixinUser = weixinUserRepository.save(weixinUser.update(weixinUserInfo.getProvince(), weixinUserInfo.getCity(),
                        weixinUserInfo.getCountry(), weixinUserInfo.getSex(), weixinUserInfo.getNickName(), weixinUserInfo.getAvatar())
                .update(weixinUserInfo.getOpenId(), weixinUserInfo.getUnionId()));
        return getUserByUnionId(weixinUserInfo, ip, weixinUser);
    }

    private User getUserByUnionId(WeixinUserInfo weixinUserInfo, String ip, WeixinUser weixinUser) {
        Accounts accounts = accountsRepository.findByWeixinUser_UnionId(weixinUserInfo.getUnionId());
        if (accounts == null) {
            accounts = accountsRepository.save(new Accounts(weixinUser));
        }
        User user = userRepository.findByAccounts(accounts);
        if (user == null) {
            user = userRepository.save(new User(accounts).updateNickName("默认昵称"));
        }
        loginLog(user.getAccounts(), ip);

        String userId = user.getId();
        new Thread(() -> {
            try {
                wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(UNOWNED, null));
                Thread.sleep(500);
                wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(USER, userId));
                Thread.sleep(500);
                wejuaiCoreClient.saveCelestialBody(new SaveCelestialBodyRequest(UNOWNED, null));
            } catch (InterruptedException e) {
                logger.error("创建星球间隔等待失败", e);
            }
        }).start();
        return user;
    }

    public void bindQq(String openId, String accessToken, Accounts accounts) {
        logger.debug("进入绑定qq的参数，openId：{}，accessToken:{}", openId, accessToken);
        QqAuthUserInfo qqUserInfo = qqBasicApi.getOauth2UserInfo(openId, accessToken);
        QqUser qqUser = qqUserRepository.findByOpenId(openId);
        if (qqUser == null) {
            qqUser = new QqUser();
        } else {
            Accounts otherAccounts = accountsRepository.findByQqUser_OpenId(openId);
            if (otherAccounts != null) {
                accountsRepository.save(otherAccounts.setQqUser(null));
            }
        }
        qqUser = qqUserRepository.save(qqUser.update(openId, qqUserInfo.getNickName(), qqUserInfo.getAvatar(), Sex.find(qqUserInfo.getSex())));
        accountsRepository.save(accounts.setQqUser(qqUser));
    }

    public void bindWeibo(String uid, String accessToken, Accounts accounts) {
        WeiboAuthUserInfo weiboUserInfo = weiboBasicApi.getOauth2UserInfo(uid, accessToken);
        WeiboUser weiboUser = weiboUserRepository.findByOpenId(uid);
        if (weiboUser == null) {
            weiboUser = new WeiboUser();
        } else {
            Accounts otherAccounts = accountsRepository.findByWeiboUser_OpenId(uid);
            if (otherAccounts != null) {
                accountsRepository.save(otherAccounts.setWeiboUser(null));
            }
        }
        weiboUser = weiboUserRepository.save(weiboUser.update(uid, weiboUserInfo.getNickName(), weiboUserInfo.getHeadImgUrl(),
                Sex.find(weiboUserInfo.getSex())));
        accountsRepository.save(accounts.setWeiboUser(weiboUser));
    }

    public void bindWeixin(WeixinUserInfo weixinUserInfo, Accounts accounts) {
        WeixinUser weixinUser = saveWeixinUser(weixinUserInfo);
        //将微信信息更新到账号，这个accounts是从user里get出来的，不会null
        accountsRepository.save(accounts.setWeixinUser(weixinUser));
    }

    public WeixinUser saveWeixinUser(WeixinUserInfo weixinUserInfo) {
        if (weixinUserInfo.getType() == WxLoginType.MINIPROGRAM) {
            throw new BadRequestException("保存微信用户信息类型错误, 小程序不会执行到这里");
        }
        //根据unionId搜索用户信息，没有就创建一个
        WeixinUser weixinUser = weixinUserRepository.findByUnionId(weixinUserInfo.getUnionId());
        if (weixinUser == null) {
            weixinUser = new WeixinUser();
        }
        weixinUser.update(weixinUserInfo.getProvince(), weixinUserInfo.getCity(), weixinUserInfo.getCountry(),
                weixinUserInfo.getSex(), weixinUserInfo.getNickName(), weixinUserInfo.getAvatar(), weixinUserInfo.getUnionId());
        if (weixinUserInfo.getType() == WxLoginType.PC) {
            weixinUser.setOpenId(weixinUserInfo.getOpenId());
        } else {
            weixinUser.setOffiOpenId(weixinUserInfo.getOpenId());
        }
        return weixinUserRepository.save(weixinUser);
    }

    public void createEmailCodeSignUp(CreateEmailCodeRequest request) {
        Accounts accounts = accountsRepository.findByEmail(request.getEmail());
        if (accounts != null) {
            throw new BadRequestException("该邮箱已经注册");
        }
        EmailCode emailCode = verificationCodeGenerator.getSignUpEmailCode(request.getEmail());
        mailPoster.sendRegistrationCode(emailCode);
        emailCodeRepository.save(emailCode);
    }

    @Transactional
    public User login(LoginRequest request, String ip) {
        Accounts accounts = accountsRepository.findByEmail(request.getEmail());
        if (accounts == null) {
            throw new BadRequestException("帐号或密码错误");
        }
        if (!accounts.login(request.getPassword())) {
            throw new BadRequestException("帐号或密码错误");
        }
        loginLog(accounts, ip);
        return getUserByUnionId(accounts);
    }

    @Transactional
    public void passwordResetSendMail(CreateEmailCodeRequest request) {
        Accounts accounts = accountsRepository.findByEmail(request.getEmail());
        if (accounts == null) {
            throw new BadRequestException("没有该帐号");
        }
        EmailCode emailCode = verificationCodeGenerator.getPasswordResetEmailCode(request.getEmail());
        emailCodeRepository.save(emailCode);
        mailPoster.sendPasswordResetMail(emailCode);
    }

    @Transactional
    public void passwordResetVerifyEmailCode(VerifyEmailCodeRequest request, HttpSession session) {
        verifyEmailCode(request.getEmail(), request.getCode());
        Accounts accounts = accountsRepository.findByEmail(request.getEmail());
        if (accounts == null) {
            throw new BadRequestException("没有该帐号");
        }
        accountsRepository.save(accounts.setPassword(request.getPassword()));
        session.removeAttribute(SecurityConfig.SESSION_LOGIN);
    }

    @Transactional
    public void editEmailSendOldMail(User user) {
        EmailCode emailCode = verificationCodeGenerator.getEditEmailEmailCode(user.getAccounts().getEmail(), user.getId());
        emailCodeRepository.save(emailCode);
        mailPoster.sendEditEmailMail(emailCode);
    }

    @Transactional
    public void editEmailSendNewMail(String email, User user) {
        EmailCode emailCode = verificationCodeGenerator.getEditEmailEmailCode(email, user.getId());
        emailCodeRepository.save(emailCode);
        mailPoster.sendEditEmailMail(emailCode);
    }

    /**
     * 修改邮箱
     *
     * @param newEmail 新邮箱
     * @param newCode  新邮箱的验证码
     * @param oldCode  旧邮箱验证码
     */
    @Transactional
    public void editEmail(User user, String newEmail, String newCode, String oldCode) {
        Accounts accounts = user.getAccounts();
        String oldEmail = user.getAccounts().getEmail();
        if (StringUtils.equals(newEmail, oldEmail)) {
            throw new BadRequestException("新邮箱和旧邮箱相同");
        }
        verifyEmailCode(newEmail, newCode);
        verifyEmailCode(oldEmail, oldCode);
        accountsRepository.save(accounts.setEmail(newEmail));
        if (StringUtils.equals(oldEmail, user.getNickName())) {
            userRepository.save(user.setNickName(newEmail));
        }
    }

    public Accounts getAccountsByQq(String openId) {
        Accounts accounts = accountsRepository.findByQqUser_OpenId(openId);
        if (accounts == null) {
            throw new BadRequestException("该授权未绑定账号");
        }
        return accounts;
    }

    public Accounts getAccountsByWeibo(String uid) {
        Accounts accounts = accountsRepository.findByWeiboUser_OpenId(uid);
        if (accounts == null) {
            throw new BadRequestException("该授权未绑定账号");
        }
        return accounts;
    }

    public Accounts getAccountsByWeixin(String unionId) {
        Accounts accounts = accountsRepository.findByWeixinUser_UnionId(unionId);
        if (accounts == null) {
            throw new BadRequestException("该授权未绑定账号, 会触发外面的cache跳转注册/绑定页面");
        }
        return accounts;
    }

    public User getUserByUnionId(Accounts accounts) {
        User user = userRepository.findByAccounts(accounts);
        if (user == null) {
            throw new ServerException("该帐号没有对应用户：" + accounts.getId());
        }
        return user;
    }

    public void loginLog(Accounts accounts, String ip) {
        new Thread(() -> {
            accountsRepository.save(accounts.setIp(ip));
            loginLogRepository.save(new LoginLog(accounts.getId(), ip));
        }).start();
    }

    public Accounts findByEmail(LoginRequest request) {
        Accounts accounts = accountsRepository.findByEmail(request.getEmail());
        if (accounts == null) {
            throw new BadRequestException("帐号未注册");
        }
        if (!accounts.login(request.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        return accounts;
    }

    public AccountInfo getAccountInfo(User user) {
        Accounts accounts = user.getAccounts();
        List<LoginLog> lastList = loginLogRepository.findTop2ByAccountsIdOrderByCreatedAtDesc(accounts.getId());
        LoginLog loginLog = lastList.get(1);
        return new AccountInfo(accounts, loginLog);
    }

    public String getAccessToken() {
        Optional<WeiXinToken> tokenOptional = weiXinTokenRepository.findById(weixinConfig.getAppId());
        if (tokenOptional.isEmpty()) {
            throw new NotFoundException("未找到微信accessToken");
        }
        return tokenOptional.get().getToken();
    }

    public WeixinUser getWeixinUserByAppOpenId(String openId) {
        return weixinUserRepository.findByAppOpenId(openId);
    }

    private void verifyEmailCode(String email, String code) {
        Optional<EmailCode> hasEmailCode = emailCodeRepository.findById(email);
        if (hasEmailCode.isEmpty()) {
            throw new BadRequestException("没有该邮箱注册信息");
        }
        EmailCode emailCode = hasEmailCode.get();
        if (DateUtil.compareByDate(emailCode.getExpiredAt(), new Date())) {
            throw new BadRequestException("验证码超时");
        }
        if (!StringUtils.equals(code, emailCode.getCode())) {
            throw new BadRequestException("邮箱验证码不正确");
        }
    }

}

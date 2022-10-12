package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.infrastructure.client.TradeGatewayClient;
import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.web.dto.request.CreateWithdrawalRequest;
import com.wejuai.dto.request.RechargeRequest;
import com.wejuai.dto.request.SaveWithdrawalRequest;
import com.wejuai.entity.mysql.ChannelType;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author ZM.Wang
 */
@Service
public class OrderService {

    private final WejuaiCoreClient wejuaiCoreClient;
    private final TradeGatewayClient tradeGatewayClient;

    public OrderService(WejuaiCoreClient wejuaiCoreClient, TradeGatewayClient tradeGatewayClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
        this.tradeGatewayClient = tradeGatewayClient;
    }

    public Map<String, String> recharge(User user, RechargeRequest request, String ip) {
        if (request.getType() == ChannelType.WEIXIN_JSAPI) {
            if (user.getAccounts().getWeixinUser() == null || StringUtils.isBlank(user.getAccounts().getWeixinUser().getOffiOpenId())) {
                throw new BadRequestException("尚未绑定公众号");
            }
            request.setOpenId(user.getAccounts().getWeixinUser().getOffiOpenId());
        }
        if (request.getType() == ChannelType.WEIXIN_MINI_APP) {
            if (user.getAccounts().getWeixinUser() == null || StringUtils.isBlank(user.getAccounts().getWeixinUser().getAppOpenId())) {
                throw new BadRequestException("尚未绑定小程序");
            }
            request.setOpenId(user.getAccounts().getWeixinUser().getAppOpenId());
        }
        return tradeGatewayClient.payment(user.getId(), request, ip);
    }

    public void privatePayment(long money, HttpServletResponse response) throws IOException {
        RechargeRequest request = new RechargeRequest(money, null, ChannelType.ALIPAY_WEB);
        Map<String, String> privatePayment = tradeGatewayClient.payment("privatePayment", request, "null");
        response.sendRedirect(privatePayment.get("html"));
    }

    public void withdrawal(User user, CreateWithdrawalRequest request) {
        SaveWithdrawalRequest coreReq = new SaveWithdrawalRequest();
        coreReq.setCardNo(request.getCardNo()).setChannelType(request.getChannelType()).setIntegral(request.getIntegral())
                .setName(request.getName()).setUserId(user.getId());
        wejuaiCoreClient.withdrawal(coreReq);
    }

}

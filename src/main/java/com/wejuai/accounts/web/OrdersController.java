package com.wejuai.accounts.web;

import com.wejuai.accounts.infrastructure.client.WejuaiCoreClient;
import com.wejuai.accounts.web.dto.request.CreateOrderAppealRequest;
import com.wejuai.dto.request.SaveOrderAppealRequest;
import com.wejuai.dto.response.*;
import com.wejuai.entity.mongo.trade.TradeStatus;
import com.wejuai.entity.mysql.ApplyStatus;
import com.wejuai.entity.mysql.ChannelQueryType;
import com.wejuai.entity.mysql.OrdersType;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "订单相关")
@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final WejuaiCoreClient wejuaiCoreClient;

    public OrdersController(WejuaiCoreClient wejuaiCoreClient) {
        this.wejuaiCoreClient = wejuaiCoreClient;
    }

    @ApiOperation("订单申诉")
    @PostMapping("/orderAppeal")
    public void orderAppeal(@SessionAttribute(SESSION_LOGIN) User user, CreateOrderAppealRequest request) {
        SaveOrderAppealRequest saveRequest = new SaveOrderAppealRequest(user.getId(), request.getType(), request.getTypeId(), request.getQuestion());
        wejuaiCoreClient.orderAppeal(saveRequest);
    }

    @ApiModelProperty("获取订单申诉列表")
    @GetMapping("/orderAppeal")
    public Slice<OrderAppealInfo> getOrderAppeals(@SessionAttribute(SESSION_LOGIN) String userId,
                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getOrderAppeals(userId, page, size);
    }

    @ApiOperation("获取订单分页")
    @GetMapping
    public Slice<OrdersInfo> getOrders(@RequestParam(required = false, defaultValue = "") OrdersType type,
                                       @RequestParam(required = false, defaultValue = "") Boolean income,
                                       @RequestParam(required = false, defaultValue = "") Long start,
                                       @RequestParam(required = false, defaultValue = "") Long end,
                                       @RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "10") int size,
                                       @SessionAttribute(SESSION_LOGIN) String userId) {
        return wejuaiCoreClient.getOrders(userId, type, income, start, end, page, size);
    }

    @ApiOperation("查询充值订单")
    @GetMapping("/charge")
    public Slice<ChargeListInfo> getChargeInfos(@RequestParam(required = false, defaultValue = "") TradeStatus status,
                                                @RequestParam(required = false, defaultValue = "") ChannelQueryType channelType,
                                                @RequestParam(required = false, defaultValue = "") Long start,
                                                @RequestParam(required = false, defaultValue = "") Long end,
                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size,
                                                @SessionAttribute(SESSION_LOGIN) String userId) {
        return wejuaiCoreClient.getChargeInfos(userId, status, channelType, start, end, page, size);
    }

    @ApiOperation("查询提现订单")
    @GetMapping("/withdrawal")
    public Slice<WithdrawalInfo> getWithdrawals(@RequestParam(required = false, defaultValue = "") ApplyStatus status,
                                                @RequestParam(required = false, defaultValue = "") Long start,
                                                @RequestParam(required = false, defaultValue = "") Long end,
                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size,
                                                @SessionAttribute(SESSION_LOGIN) String userId) {
        return wejuaiCoreClient.getWithdrawals(userId, status, start, end, page, size);
    }

    @ApiOperation("查询积分详细信息")
    @GetMapping("/integral")
    public UserIntegralInfo getUserIntegral(@SessionAttribute(SESSION_LOGIN) String userId) {
        return wejuaiCoreClient.getUserIntegral(userId);
    }

    @ApiOperation("已购列表")
    @GetMapping("/purchased")
    public Slice<ArticleListInfo> getPurchased(@SessionAttribute(SESSION_LOGIN) String userId,
                                               @RequestParam(required = false, defaultValue = "") String titleStr,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return wejuaiCoreClient.getPurchased(userId, titleStr, page, size);
    }
}

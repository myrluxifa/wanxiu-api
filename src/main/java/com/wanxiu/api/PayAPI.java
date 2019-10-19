package com.wanxiu.api;

import com.wanxiu.common.AccountParam;
import com.wanxiu.common.Common;
import com.wanxiu.config.CustomConfig;
import com.wanxiu.dto.PayDto;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveOrder;
import com.wanxiu.entity.LiveSysRatio;
import com.wanxiu.repository.LiveSysRatioRepository;
import com.wanxiu.service.AccountService;
import com.wanxiu.service.GiftService;
import com.wanxiu.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@Api(value="/orderPay",description="支付统一接口")
@RestController
public class PayAPI {

    @Autowired
    private PayService payService;

    @Autowired
    private WxPayAPI wxPayAPI;

    @Autowired
    private AliPayAPI aliPayAPI;

    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private GiftService giftService;

    @Autowired
    private LiveSysRatioRepository liveSysRatioRepository;

    @Autowired
    private AccountService accountService;


    @ApiOperation(value="支付统一接口",notes="")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType="query", name = "payType", value = "支付方式（2.微信；3.支付宝）", required = true, dataType = "String"),
                    @ApiImplicitParam(paramType="query", name = "pay", value = "金额", required = true, dataType = "String"),
                    @ApiImplicitParam(paramType="query", name = "ip", value = "ip地址", required = true, dataType = "String"),
                    @ApiImplicitParam(paramType="query", name = "userId", value = "用户编号", required = true, dataType = "String")
            }
    )
    @RequestMapping(value="/pay",method=RequestMethod.POST)
    public ResEntity pay(String payType,String pay,String ip,String userId) {

        //用途

        String wx_notify=customConfig.getUrl()+"/wxController/rechargeNotify";
        String ali_notify=customConfig.getUrl()+"/aliController/rechargNotify";
        String body="万秀直播-充值";

        LiveOrder order;
        LiveSysRatio liveSysRatio=liveSysRatioRepository.findAll().iterator().next();
        try {
            order=payService.saveOrder(new LiveOrder(
                    userId,//用户编号
                    new BigDecimal(pay),//支付金额单位为分
                    Integer.valueOf(payType),//支付方式（2 微信支付 3支付宝支付）
                    1,//支付状态 1未支付 2已支付
                    new Date(),
                    0,
                    "",//被刷礼物主播的id
                    "",
                    new BigDecimal(pay).multiply(new BigDecimal(liveSysRatio.getRatio())).intValue()
                    ));
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResEntity(Common.RESULT.FAIL,new PayDto());
        }
        if(payType.equals("2")) {
            //微信精确到分
            return wxPayAPI.recharge(userId,String.valueOf(new BigDecimal(pay).multiply(new BigDecimal("100")).intValue()), ip, order.getId(),wx_notify,body);
        }else if(payType.equals("3")){
            return aliPayAPI.aliPay(userId, new BigDecimal(pay).setScale(2).toString(), ip, order.getId(),ali_notify,body);
        }else {
            return new ResEntity(Common.RESULT.FAIL,new PayDto());
        }
    }




    @ApiOperation(value="购买守护",notes="")
    @RequestMapping(value = "/buyGuardian",method = RequestMethod.POST)
    public ResEntity buyGuardian(String guardianId,String userId,String showId){
            ResEntity resEntity=giftService.buyGuardian(userId,Integer.valueOf(showId),guardianId);
            return resEntity;
    }



//    public ResEntity nativePayResUrl() {
//
//    }





}

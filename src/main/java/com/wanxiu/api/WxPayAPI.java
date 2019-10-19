package com.wanxiu.api;


import com.wanxiu.common.Common;
import com.wanxiu.config.CustomConfig;
import com.wanxiu.config.WechatConfig;
import com.wanxiu.dto.PayDto;
import com.wanxiu.dto.QRBNativeDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveOrder;
import com.wanxiu.entity.LiveSysRatio;
import com.wanxiu.entity.WxPayNotifyLog;
import com.wanxiu.repository.LiveSysRatioRepository;
import com.wanxiu.repository.WxPayNotifyLogRepository;
import com.wanxiu.service.GiftService;
import com.wanxiu.service.PayService;
import com.wanxiu.util.QRBarCodeUtil;
import com.wanxiu.util.Util;
import com.wanxiu.util.WeixinPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
public class WxPayAPI {

    @Autowired
    private PayService payService;

    @Autowired
    private WechatConfig weixin;

    @Autowired
    private WxPayNotifyLogRepository wxPayNotifyLogRepository;

    @Autowired
    private LiveSysRatioRepository liveSysRatioRepository;

    @Autowired
    private GiftService giftService;

    @Autowired
    private CustomConfig customConfig;

    private static final Logger log = LoggerFactory.getLogger(WxPayAPI.class);

    @RequestMapping(value="/recharge",method= RequestMethod.POST)
    @ResponseBody
    public ResEntity recharge(@RequestParam("loginId") String loginId, @RequestParam("pay") String pay, @RequestParam("ip") String ip, String orderId, String notify, String body){
        try {
            log.info("用户编号："+loginId+";请求充值："+pay);
            //微信 payType:2 充值分类：4
            //PayOrder order=payOrderService.save(new PayOrder(Integer.valueOf(loginId),new BigDecimal(pay).divide(new BigDecimal(100)),2,1,new Date(),Integer.valueOf(type),Util.getUUID()));

            SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
            parameterMap.put("appid", weixin.getAppid());
            parameterMap.put("mch_id", weixin.getMachId());
            parameterMap.put("nonce_str", WeixinPayUtil.getRandomString(32));
            parameterMap.put("body",body);
            parameterMap.put("out_trade_no", orderId);
            parameterMap.put("fee_type", "CNY");
            parameterMap.put("total_fee", pay);
            parameterMap.put("spbill_create_ip", ip);
            parameterMap.put("notify_url", notify);
            parameterMap.put("trade_type", "APP");
            String sign = WeixinPayUtil.createSign(parameterMap);
            parameterMap.put("sign", sign);
            String requestXML = WeixinPayUtil.getRequestXml(parameterMap);
            log.info(requestXML);
            String result = WeixinPayUtil.httpsRequest(
                    "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",
                    requestXML);
            log.info(result);
            Map<String, String> map = null;
            try {
                map = WeixinPayUtil.doXMLParse(result);
                if(map.get("return_code").equals("SUCCESS")&&map.get("result_code").equals("SUCCESS")){

                   //这里写日志  暂时还没写呢
                    wxPayNotifyLogRepository.save(new WxPayNotifyLog(map,map.get("trade_type")));
                    return new ResEntity(Common.RESULT.SUCCESS,
                            new PayDto(payService.getwxPayDto(weixin.getAppid(), weixin.getMachId(), String.valueOf(map.get("prepay_id")), WeixinPayUtil.getRandomString(32), String.valueOf(new Date().getTime()/1000)))
                    );
                }else {
                    return new ResEntity(Common.RESULT.WECHAT_RECHARGE_ERROR,new PayDto());
                }
            }catch(Exception e) {
                e.printStackTrace();
                return new ResEntity(Common.RESULT.WECHAT_RECHARGE_ERROR,new PayDto());
            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResEntity(Common.RESULT.WECHAT_RECHARGE_ERROR,new PayDto());
        }
    }


    @RequestMapping(value = "/withdraw",method = RequestMethod.POST)
    public ResEntity withdraw(String partnerTradeNo,String openId,String amount,String ip){
        try{
            SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
            parameterMap.put("mch_appid", weixin.getAppid());
            parameterMap.put("mchid", weixin.getMachId());
            parameterMap.put("device_info", "");
            parameterMap.put("nonce_str",WeixinPayUtil.getRandomString(32));
            parameterMap.put("partner_trade_no", partnerTradeNo);
            parameterMap.put("openid",openId);
            parameterMap.put("check_name","NO_CHECK");
            parameterMap.put("re_user_name","");
            parameterMap.put("amount", amount);
            parameterMap.put("desc", "提现礼物收益到个人账户");
            parameterMap.put("spbill_create_ip", ip);

            String sign = WeixinPayUtil.createSign(parameterMap);
            parameterMap.put("sign", sign);

            String requestXML = WeixinPayUtil.getRequestXml(parameterMap);
            log.info(requestXML);
            String result = WeixinPayUtil.doRefund(weixin.getMachId(),
                    "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers",
                    requestXML);
            log.info(result);
            Map<String, String> map = null;
            try {
                map = WeixinPayUtil.doXMLParse(result);
                if(map.get("return_code").equals("SUCCESS")&&map.get("result_code").equals("SUCCESS")){

                    //这里写日志  暂时还没写呢

                    return new ResEntity(Common.RESULT.SUCCESS);
                }else {
                    return new ResEntity(Common.RESULT.WECHAT_RECHARGE_ERROR,new PayDto());
                }
            }catch(Exception e) {
                e.printStackTrace();
                return new ResEntity(Common.RESULT.WECHAT_RECHARGE_ERROR,new PayDto());
            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResEntity(Common.RESULT.WECHAT_RECHARGE_ERROR,new PayDto());
        }
    }

    @RequestMapping(value = "/createNative",method = RequestMethod.POST)
    public ResEntity createNative(String userId,String giftId,String count){
        String price=giftService.giftPrice(giftId);
        log.info(price);
        String priceCoin=giftService.giftPriceCoin(giftId);
        LiveOrder order;
        //LiveSysRatio liveSysRatio=liveSysRatioRepository.findAll().iterator().next();
        try {
            BigDecimal pay=new BigDecimal(price).multiply(new BigDecimal(count));
            log.info("需要付款："+pay.toString());
            order=payService.saveOrder(new LiveOrder(
                    userId,//用户编号
                    pay,//支付金额单位为分
                    Integer.valueOf(2),//支付方式（2 微信支付 3支付宝支付）
                    1,//支付状态 1未支付 2已支付
                    new Date(),
                    0,
                    "",//被刷礼物主播的id
                    "",
                    new BigDecimal(priceCoin).multiply(new BigDecimal(count)).intValue()
            ));

            String wx_notify=customConfig.getUrl()+"/wxController/rechargeNotify";

            String body="万秀直播-充值";

//            recharge(userId,String.valueOf(pay.intValue()), "152.136.34.178", order.getId(),wx_notify,body);

            Date d=new Date();
            String timeStemp=String.valueOf(d.getTime());
            timeStemp=timeStemp.substring(0,9);
            SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
            parameterMap.put("appid", weixin.getAppid());
            parameterMap.put("mch_id", weixin.getMachId());
            parameterMap.put("time_stamp",timeStemp);
            parameterMap.put("nonce_str",WeixinPayUtil.getRandomString(32));
            parameterMap.put("product_id",order.getId());
            String sign = WeixinPayUtil.createSign(parameterMap);
            parameterMap.put("sign",sign);
            log.info("签名"+sign);
            String requestXML = WeixinPayUtil.getRequestXml(parameterMap);
            log.info(requestXML);

            StringBuffer sb=new StringBuffer("weixin://wxpay/bizpayurl?sign=");
            sb.append(sign)
                    .append("&appid=")
                    .append(weixin.getAppid())
                    .append("&mch_id=")
                    .append(weixin.getMachId())
                    .append("&product_id=")
                    .append(order.getId())
                    .append("&time_stamp=")
                    .append(timeStemp)
                    .append("&nonce_str=")
                    .append(parameterMap.get("nonce_str"));
            String fileName=Util.getUUID()+".jpg";
            log.info(sb.toString());
            QRBarCodeUtil.createCodeToFile(sb.toString(),new File("/home/wxlive/image"),fileName);

            return new ResEntity(Common.RESULT.SUCCESS,new QRBNativeDTO(customConfig.getUrl()+"/"+fileName,order.getId()));
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResEntity(Common.RESULT.FAIL);
        }
    }


//    @RequestMapping(value="/nativePayResUrl",method= RequestMethod.GET)
//    public ResEntity nativePayResUrl(){
//        log.info("native支付回调");
//        return new ResEntity(Common.RESULT.SUCCESS);
//    }
}

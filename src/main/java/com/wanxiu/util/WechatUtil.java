package com.wanxiu.util;

import com.wanxiu.dto.wechat.WechatUnifiedorder;

public class WechatUtil {

    public static String wechatSign(WechatUnifiedorder wechatUnifiedorder){

        String sign="appid="+wechatUnifiedorder.getAppid()+
                "mch_id="+wechatUnifiedorder.getMch_id()+
                "nonce_str="+wechatUnifiedorder.getNonce_str()+
                "body="+wechatUnifiedorder.getBody()+
                "out_trade_no="+wechatUnifiedorder.getOut_trade_no()+
                "total_fee="+wechatUnifiedorder.getTotal_fee()+
                "spbill_create_ip="+wechatUnifiedorder.getSpbill_create_ip()+
                "notify_url="+wechatUnifiedorder.getNotify_url()+
                "trade_type="+wechatUnifiedorder.getTrade_type()
                ;
        return sign;
    }
}

package com.wanxiu.dto.wechat;

import lombok.Data;

@Data
public class WxWithdrawReq {
    private String mch_appid;

    private String mchid;

    private String device_info;

    private String nonce_str;

    private String sign;

    private String partner_trade_no;

    private String openid;

    private String check_name;

    private String re_user_name;

    private String amount;

    private String desc;

    private String spbill_create_ip;


}

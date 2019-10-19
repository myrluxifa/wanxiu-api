package com.wanxiu.dto.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@XStreamAlias("wechatUnifiedorder")
@Getter
@Setter
public class WechatUnifiedorder {
    private String appid;

    private String mch_id;

    private String device_info;

    private String nonce_str;

    private String sign;

    private String sign_type;

    private String body;

    private String detail;

    private String attach;

    private String out_trade_no;

    private String fee_type;

    private String total_fee;

    private String spbill_create_ip;

    private String time_start;

    private String time_exprie;

    private String goods_tag;

    private String notify_url;

    private String trade_type;

    private String limit_pay;

    private String receipt;

}

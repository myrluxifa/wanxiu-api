package com.wanxiu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class WechatConfig {
    @Value("${custom.wechat.appid}")
    private String appid;
    @Value("${custom.wechat.secret}")
    private String secret;

    @Value("${custom.wechat.appid2}")
    private String appid2;
    @Value("${custom.wechat.secret2}")
    private String secret2;

    @Value("${custom.wechat.mchId}")
    private String machId;

    @Value("${custom.wechat.deviceInfo}")
    private String deviceInfo;


    @Value("${custom.wechat.feeType}")
    private String feeType;

    @Value("${custom.wechat.tradeType}")
    private String tradeType;

    @Value("${custom.wechat.key}")
    private String key;
}

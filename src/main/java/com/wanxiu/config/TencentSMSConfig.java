package com.wanxiu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TencentSMSConfig {
    @Value("${custom.tencent.sms.appid}")
    private int appid;

    @Value("${custom.tencent.sms.appkey}")
    private String appkey;
}

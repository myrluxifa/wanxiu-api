package com.wanxiu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TencentIMConfig {
    @Value("${custom.tencent.IM.sdk_appid}")
    private int sdkappid;
    @Value("${custom.tencent.IM.private_key}")
    private String privateKey;
}

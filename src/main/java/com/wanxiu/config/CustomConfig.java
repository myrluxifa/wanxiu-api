package com.wanxiu.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CustomConfig {

    @Value("${custom.url.prefix}")
    private String url;
}

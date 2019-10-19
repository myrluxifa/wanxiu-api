package com.wanxiu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenDTO {
    private String access_token;

    private String expires_in;

    private String refresh_token;

    private String openid;

    private String scope;

    private String uniond;

    private String errcode;

    private String errmsg;
}

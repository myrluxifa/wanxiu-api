package com.wanxiu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class AliPayConfig {
	
	@Value("${custom.ali.pay.appId}")
	private String appId;
	
	@Value("${custom.ali.pay.appPrivateKey}")
	private String appPrivateKey;
	
	@Value("${custom.ali.pay.aliPublicKey}")
	private String aliPublicKey;

	@Value("${custom.ali.pay.notifyUrl}")
	private String notifyUrl;


	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppPrivateKey() {
		return appPrivateKey;
	}

	public void setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
	}

	public String getAliPublicKey() {
		return aliPublicKey;
	}

	public void setAliPublicKey(String aliPublicKey) {
		this.aliPublicKey = aliPublicKey;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
}

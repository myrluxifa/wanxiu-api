package com.wanxiu.dto.ali;

public class AliPayRecharge {
	private String responseBody;
	
	public AliPayRecharge() {
		// TODO Auto-generated constructor stub
	}
	
	public AliPayRecharge(String responseBody) {
		this.responseBody=responseBody;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

}

package com.wanxiu.dto;

import com.wanxiu.dto.ali.AliPayRecharge;
import com.wanxiu.dto.wechat.WxRechargeDto;

public class PayDto {
	
	private AliPayRecharge aliPayRecharge;
	
	private WxRechargeDto wxRechargeDto;
	
	public PayDto() {
		// TODO Auto-generated constructor stub
	}
	
	public PayDto(AliPayRecharge aliPayRecharge) {
		// TODO Auto-generated constructor stub
		this.aliPayRecharge=aliPayRecharge;
	}
	
	public PayDto(WxRechargeDto wxRechargeDto) {
		// TODO Auto-generated constructor stub
		this.wxRechargeDto=wxRechargeDto;
	}

	public AliPayRecharge getAliPayRecharge() {
		return aliPayRecharge;
	}

	public void setAliPayRecharge(AliPayRecharge aliPayRecharge) {
		this.aliPayRecharge = aliPayRecharge;
	}

	public WxRechargeDto getWxRechargeDto() {
		return wxRechargeDto;
	}

	public void setWxRechargeDto(WxRechargeDto wxRechargeDto) {
		this.wxRechargeDto = wxRechargeDto;
	}
	
	
}

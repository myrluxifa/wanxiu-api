package com.wanxiu.dto.wechat;

public class WxRechargeDto {
	private String partnerid;
	
	private String prepayid;
	
	private String noncestr;
	
	private String  timestamp;
	
	private String sign;
	
	private String packagevalue;
	
	public WxRechargeDto() {
		// TODO Auto-generated constructor stub
	}
	
	public WxRechargeDto(String partnerid,String prepayid,String noncestr,String timestamp,String sign,String packagevalue) {
		// TODO Auto-generated constructor stub
		this.partnerid=partnerid;
		this.prepayid=prepayid;
		this.noncestr=noncestr;
		this.timestamp=timestamp;
		this.sign=sign;
		this.packagevalue=packagevalue;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPackagevalue() {
		return packagevalue;
	}

	public void setPackagevalue(String packagevalue) {
		this.packagevalue = packagevalue;
	}
	

	
}

package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "wx_pay_notify_log")
public class WxPayNotifyLog  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "return_code")
	private String returnCode;

	@Column(name = "return_msg")
	private String returnMsg;

	private String appid;

	@Column(name = "mch_id")
	private String mchId;

	@Column(name = "device_info")
	private String deviceInfo;

	@Column(name = "nonce_str")
	private String nonceStr;

	private String sign;

	@Column(name = "result_code")
	private String resultCode;

	@Column(name = "err_code")
	private String errCode;

	@Column(name = "err_code_des")
	private String errCodeDes;

	private String openid;

	@Column(name = "is_subscribe")
	private String isSubscribe;

	@Column(name = "trade_type")
	private String tradeType;

	@Column(name = "bank_type")
	private String bankType;

	@Column(name = "total_fee")
	private Integer totalFee;

	@Column(name = "fee_type")
	private String feeType;

	@Column(name = "cash_fee")
	private Integer cashFee;

	@Column(name = "cash_fee_type")
	private String cashFeeType;

	@Column(name = "coupon_fee")
	private Integer couponFee;

	@Column(name = "coupon_count")
	private Integer couponCount;

	@Column(name = "coupon_id_$n")
	private String couponId$n;

	@Column(name = "coupon_fee_$n")
	private Integer couponFee$n;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "out_trade_no")
	private String outTradeNo;

	private String attach;

	@Column(name = "time_end")
	private String timeEnd;

	@Column(name = "create_time")
	private java.util.Date createTime;

	public WxPayNotifyLog() {
	}

	public WxPayNotifyLog(Map<Object, Object> map) {
		this.returnCode=map.get("return_code")==null?"":String.valueOf(map.get("return_code"));
		this.returnMsg=map.get("return_msg")==null?"":String.valueOf(map.get("return_msg"));
		this.appid=map.get("appid")==null?"":String.valueOf(map.get("appid"));
		this.mchId=map.get("mch_id")==null?"":String.valueOf(map.get("mch_id"));
		this.deviceInfo=map.get("device_info")==null?"":String.valueOf(map.get("device_info"));
		this.nonceStr=map.get("nonce_str")==null?"":String.valueOf(map.get("nonce_str"));
		this.sign=map.get("sign")==null?"":String.valueOf(map.get("sign"));
		this.resultCode=map.get("result_code")==null?"":String.valueOf(map.get("result_code"));
		this.errCode=map.get("err_code")==null?"":String.valueOf(map.get("err_code"));
		this.errCodeDes=map.get("err_code_des")==null?"":String.valueOf(map.get("err_code_des"));
		this.openid=map.get("openid")==null?"":String.valueOf(map.get("openid"));
		this.isSubscribe=map.get("is_subscribe")==null?"":String.valueOf(map.get("is_subscribe"));
		this.tradeType=map.get("trade_type")==null?"":String.valueOf(map.get("trade_type"));
		this.bankType=map.get("bank_type")==null?"":String.valueOf(map.get("bank_type"));
		this.totalFee=map.get("total_fee")==null?0:Integer.valueOf(String.valueOf(map.get("total_fee")));
		this.feeType=map.get("fee_type")==null?"":String.valueOf(map.get("fee_type"));
		this.cashFee=map.get("cash_fee")==null?0:Integer.valueOf(String.valueOf(map.get("cash_fee")));
		this.cashFeeType=map.get("cash_fee_type")==null?"":String.valueOf(map.get("cash_fee_type"));
		this.couponFee=map.get("coupon_fee")==null?0:Integer.valueOf(String.valueOf(map.get("coupon_fee")));
		this.couponCount=map.get("coupon_count")==null?0:Integer.valueOf(String.valueOf(map.get("coupon_count")));
		this.couponId$n=map.get("coupon_id_$n")==null?"":String.valueOf(map.get("coupon_id_$n"));
		this.couponFee$n=map.get("coupon_fee_$n")==null?0:Integer.valueOf(String.valueOf(map.get("coupon_fee_$n")));
		this.transactionId=map.get("transaction_id")==null?"":String.valueOf(map.get("transaction_id"));
		this.outTradeNo=map.get("out_trade_no")==null?"":String.valueOf(map.get("out_trade_no"));
		this.attach=map.get("attach")==null?"":String.valueOf(map.get("attach"));
		this.timeEnd=map.get("time_end")==null?"":String.valueOf(map.get("time_end"));
		this.createTime=new Date();
	}

	public WxPayNotifyLog(Map<String, String> map,String tradeType) {
		// TODO Auto-generated constructor stub
		this.appid=map.get("appid");
		this.mchId=map.get("mach_id");
		this.deviceInfo=map.get("device_info");
		this.nonceStr=map.get("nonce_str");
		this.sign=map.get("sign");
		this.resultCode=map.get("result_code");
		this.errCode=map.get("err_code");
		this.tradeType=tradeType;
	}

}

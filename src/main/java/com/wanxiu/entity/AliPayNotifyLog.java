package com.wanxiu.entity;

import com.wanxiu.util.Util;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Entity
@Table(name = "ali_pay_notify_log")
public class AliPayNotifyLog  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "notify_time")
	private java.util.Date notifyTime;

	@Column(name = "notify_type")
	private String notifyType;

	@Column(name = "notify_id")
	private String notifyId;

	@Column(name = "app_id")
	private String appId;

	private String charset;

	private String version;

	@Column(name = "sign_type")
	private String signType;

	private String sign;

	@Column(name = "trade_no")
	private String tradeNo;

	@Column(name = "out_trade_no")
	private String outTradeNo;

	@Column(name = "out_biz_no")
	private String outBizNo;

	@Column(name = "buyer_id")
	private String buyerId;

	@Column(name = "buyer_logon_id")
	private String buyerLogonId;

	@Column(name = "seller_id")
	private String sellerId;

	@Column(name = "seller_email")
	private String sellerEmail;

	@Column(name = "trade_status")
	private String tradeStatus;

	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	@Column(name = "receipt_amount")
	private BigDecimal receiptAmount;

	@Column(name = "invoice_amount")
	private BigDecimal invoiceAmount;

	@Column(name = "buyer_pay_amount")
	private BigDecimal buyerPayAmount;

	@Column(name = "point_amount")
	private BigDecimal pointAmount;

	@Column(name = "refund_fee")
	private BigDecimal refundFee;

	private String subject;

	private String body;

	@Column(name = "gmt_create")
	private java.util.Date gmtCreate;

	@Column(name = "gmt_payment")
	private java.util.Date gmtPayment;

	@Column(name = "gmt_refund")
	private java.util.Date gmtRefund;

	@Column(name = "gmt_close")
	private java.util.Date gmtClose;

	@Column(name = "fund_bill_list")
	private String fundBillList;

	@Column(name = "passback_params")
	private String passbackParams;

	@Column(name = "voucher_detail_list")
	private String voucherDetailList;


	public AliPayNotifyLog(Map<String,String> params) {
		this.appId=params.get("app_id");
		this.body=params.get("body");
		this.buyerId=params.get("buyer_id");
		this.buyerLogonId=params.get("buyer_logon_id");
		this.buyerPayAmount=params.get("buyer_pay_amount")==null?new BigDecimal("0"):new BigDecimal(params.get("buyer_pay_amount"));
		this.charset=params.get("charset");
		this.fundBillList=params.get("fund_bill_list");
		this.gmtClose=params.get("gmt_close")==null?null: Util.notifyTimeFormate(params.get("gmt_close"));
		this.gmtCreate=params.get("gmt_create")==null?null:Util.notifyTimeFormate(params.get("gmt_create"));
		this.gmtPayment=params.get("gmt_payment")==null?null:Util.notifyTimeFormate(params.get("gmt_payment"));
		this.gmtRefund=params.get("gmt_refund")==null?null:Util.notifyTimeFormate(params.get("gmt_refund"));
		this.invoiceAmount=params.get("invoice_amount")==null?new BigDecimal("0"):new BigDecimal(params.get("invoice_amount"));
		this.notifyId=params.get("notify_id");
		this.notifyTime=params.get("notify_time")==null?null:Util.notifyTimeFormate(params.get("notify_time"));
		this.notifyType=params.get("notify_type");
		this.outBizNo=params.get("out_biz_no");
		this.outTradeNo=params.get("out_trade_no");
		this.passbackParams=params.get("passback_params");
		this.pointAmount=params.get("point_amount")==null?new BigDecimal("0"):new BigDecimal(params.get("point_amount"));
		this.receiptAmount=params.get("receipt_amount")==null?new BigDecimal("0"):new BigDecimal(params.get("receipt_amount"));
		this.refundFee=params.get("refund_fee")==null?new BigDecimal("0"):new BigDecimal(params.get("refund_fee"));
		this.sellerEmail=params.get("seller_email");
		this.sellerId=params.get("seller_id");
		this.sign=params.get("sign");
		this.signType=params.get("sign_type");
		this.subject=params.get("subject");
		this.totalAmount=params.get("total_amount")==null?new BigDecimal("0"):new BigDecimal(params.get("total_amount"));
		this.tradeNo=params.get("trade_no");
		this.tradeStatus=params.get("trade_status");
		this.version=params.get("version");
		this.voucherDetailList=params.get("voucher_detail_list");

	}

	public AliPayNotifyLog() {
	}
}

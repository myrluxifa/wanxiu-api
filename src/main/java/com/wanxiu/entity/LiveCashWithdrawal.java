package com.wanxiu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "live_cash_withdrawal")
public class LiveCashWithdrawal  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	private Integer coin;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private java.util.Date createTime;

	private Integer status;

	@Column(name = "approve_user")
	private String approveUser;

	@Column(name = "approve_time")
	private java.util.Date approveTime;

	@Column(name = "cash_fee")
	private BigDecimal cashFee;


	public LiveCashWithdrawal(){

	}

	public LiveCashWithdrawal(String userId, Integer coin, Date createTime, Integer status, BigDecimal cashFee) {
		this.userId = userId;
		this.coin = coin;
		this.createTime = createTime;
		this.status = status;
		this.cashFee = cashFee;
	}
}

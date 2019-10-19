package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "live_cash_account")
public class LiveCashAccount  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	private BigDecimal fee;

	@Column(name = "withdraw_fee")
	private Double withdrawFee;

	@Column(name = "update_time")
	private java.util.Date updateTime;

	public LiveCashAccount() {
	}

	public LiveCashAccount(String userId, BigDecimal fee, Date updateTime) {
		this.userId = userId;
		this.fee = fee;
		this.updateTime = updateTime;
	}
}

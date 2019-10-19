package com.wanxiu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_account")
public class LiveAccount  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	private Integer coins;

	private Integer fee;

	@Column(name = "withdraw_amount")
	private Integer withdrawAmount;

	@Column(name = "gift_amount")
	private Integer giftAmount;

	@Column(name = "income_amount")
	private Integer incomeAmount;

	@Column(name = "expend_amount")
	private Integer expendAmount;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "updat_time")
	private java.util.Date updatTime;

	private String remark;

	public LiveAccount(){

	}

	public LiveAccount(String userId, Integer coins, Integer fee, Integer withdrawAmount, Integer giftAmount, Integer incomeAmount, Integer expendAmount, Date createTime, Date updatTime, String remark) {
		this.userId = userId;
		this.coins = coins;
		this.fee = fee;
		this.withdrawAmount = withdrawAmount;
		this.giftAmount = giftAmount;
		this.incomeAmount = incomeAmount;
		this.expendAmount = expendAmount;
		this.createTime = createTime;
		this.updatTime = updatTime;
		this.remark = remark;
	}
}

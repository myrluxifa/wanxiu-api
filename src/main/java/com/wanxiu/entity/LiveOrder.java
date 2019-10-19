package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "live_order")
public class LiveOrder  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "pay_type")
	private Integer payType;

	@Column(name = "pay_state")
	private Integer payState;

	private BigDecimal pay;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "out_of_id")
	private String outOfId;

	@Column(name = "use_type")
	private Integer useType;

	@Column(name = "project_id")
	private String projectId;


	@Column(name = "pay_explain")
	private String payExplain;

	@Column(name="coin")
	private int coin;

	public LiveOrder() {
		// TODO Auto-generated constructor stub
	}

	public LiveOrder(String userId, BigDecimal pay, int payType , int payState, Date createTime, int type, String outOfId, String projectId,int coin) {
		// TODO Auto-generated constructor stub
		this.userId=userId;
		this.pay=pay;
		this.payType=payType;
		this.payState=payState;
		this.createTime=createTime;
		this.useType=type;
		this.outOfId=outOfId;
		this.projectId=projectId;
		this.coin=coin;
	}

}

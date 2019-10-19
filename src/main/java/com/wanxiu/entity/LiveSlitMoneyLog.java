package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "live_slit_money_log")
public class LiveSlitMoneyLog  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "slit_type")
	private String slitType;

	private BigDecimal money;

	private Integer ratio;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "customer_user")
	private String customerUser;

	@Column(name = "use_type")
	private String useType;

	@Column(name = "show_id")
	private String showId;

	private Integer coin;

	public LiveSlitMoneyLog() {
	}

	public LiveSlitMoneyLog(String slitType, BigDecimal money, Integer ratio, Date createTime, String customerUser, String useType, String showId, Integer coin) {
		this.slitType = slitType;
		this.money = money;
		this.ratio = ratio;
		this.createTime = createTime;
		this.customerUser = customerUser;
		this.useType = useType;
		this.showId = showId;
		this.coin = coin;
	}
}

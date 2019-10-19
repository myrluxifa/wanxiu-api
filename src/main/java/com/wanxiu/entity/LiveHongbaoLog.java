package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_hongbao_log")
public class LiveHongbaoLog  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	private Integer amount;

	@Column(name = "hongbao_id")
	private String hongbaoId;

	@Column(name = "create_time")
	private java.util.Date createTime;

	public LiveHongbaoLog(){}

	public LiveHongbaoLog(String userId, Integer amount, String hongbaoId, Date createTime) {
		this.userId = userId;
		this.amount = amount;
		this.hongbaoId = hongbaoId;
		this.createTime = createTime;
	}
}

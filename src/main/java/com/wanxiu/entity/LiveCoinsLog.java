package com.wanxiu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_coins_log")
public class LiveCoinsLog  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String type;

	@Column(name = "target_id")
	private String targetId;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "old_coins")
	private Integer oldCoins;

	private Integer coins;

	@Column(name = "new_coins")
	private Integer newCoins;

	private Integer fee;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "create_user")
	private String createUser;

	private String remark;

}

package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_gift")
public class LiveGift  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "icon_url")
	private String iconUrl;

	private String name;

	@Column(name = "show_coin")
	private Integer showCoin;

	@Column(name = "animation_url")
	private String animationUrl;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "create_user")
	private String createUser;

	@Column(name = "status")
	private Integer status;

	@Column(name="five_times_url")
	private String fiveTimesUrl;

	@Column(name="ten_times_url")
	private String tenTimesUrl;

	@Column(name="ninetynine_times_url")
	private String ninetynineTimesUrl;

	@Column(name="if_more_than_once")
	private int ifMoreThanOnce;

	@Column(name="gift_type")
	private int giftType;
}

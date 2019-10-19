package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_sys_guardian")
public class LiveSysGuardian  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String name;

	@Column(name = "show_coin")
	private Integer showCoin;

	@Column(name = "time_limit")
	private Integer timeLimit;

	@Column(name = "animation_url")
	private String animationUrl;

	@Column(name = "image_url")
	private String imageUrl;

	private String describe;

	private Integer status;

}

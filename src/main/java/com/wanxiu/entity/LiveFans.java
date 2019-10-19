package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_fans")
public class LiveFans  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String fans;

	private String idol;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Transient
	private String nickname;
	@Transient
	private String headPortrait;
	@Transient
	private long experience;
	@Transient
	private String signature;
	@Transient
	private String sex;

	public LiveFans(){

	}
	public LiveFans(Object[] o) {
		this.fans = String.valueOf(o[1]==null?"":o[1]);
		this.idol = String.valueOf(o[2]==null?"":o[2]);
		this.nickname = String.valueOf(o[4]==null?"":o[4]);
		this.headPortrait = String.valueOf(o[5]==null?"":o[5]);
		this.experience = Integer.valueOf(o[6]==null?"0":String.valueOf(o[6]));
		this.signature = String.valueOf(o[7]==null?"":o[7]);
		this.sex = String.valueOf(o[8]==null?"":o[8]);
	}
}

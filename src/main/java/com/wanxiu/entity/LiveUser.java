package com.wanxiu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_user")
@DynamicUpdate
@DynamicInsert
public class LiveUser  implements Serializable {

	private static final long serialVersionUID = -3237449701607233834L;
	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String part;

	@Column(name = "user_name")
	private String userName;

	@JsonIgnore
	private String password;

	@JsonIgnore
	@Column(name = "open_id")
	private String openId;

	@JsonIgnore
	private String token;

	@Column(name = "create_time")
	private java.util.Date createTime;

	private String nickname;

	private Integer sex;

	private String birthday;

	private String signature;

	private Integer status;

	@Column(name = "approval_status")
	private Integer approvalStatus;

	@Column(name = "show_id")
	private Integer showId;

	private Integer experience;

	private String headPortrait;

	private String background;

	//等级
	private String level;

	@OneToOne
	@JoinColumn(name = "level", insertable = false, updatable = false)
	private LiveSysLevel levelInfo;

	@Transient
	private boolean isfans;

	private Integer fans;
	private Integer praise;
	private Integer streaming;
	@Column(name = "last_show_time")
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private Date lastShowTime;
}

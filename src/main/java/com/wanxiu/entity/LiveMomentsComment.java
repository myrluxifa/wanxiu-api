package com.wanxiu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_moments_comment")
public class LiveMomentsComment  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String comment;

	private Integer status;

	@Column(name = "create_user")
	private String createUser;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "call_user")
	private String callUser;

	private String momentId;

	@OneToOne
	@JoinColumn(name = "create_user", referencedColumnName = "id", insertable = false, updatable = false)
	private LiveUser user;

	public LiveMomentsComment() {
	}

	public LiveMomentsComment(String comment, Integer status, String createUser, Date createTime,String momentId) {
		this.comment = comment;
		this.status = status;
		this.createUser = createUser;
		this.createTime = createTime;
		this.momentId=momentId;
	}

	public LiveMomentsComment(String comment, Integer status, String createUser, Date createTime, String callUser,String momentId) {
		this.comment = comment;
		this.status = status;
		this.createUser = createUser;
		this.createTime = createTime;
		this.callUser = callUser;
		this.momentId=momentId;
	}
}

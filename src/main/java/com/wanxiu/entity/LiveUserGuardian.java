package com.wanxiu.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_user_guardian")
public class LiveUserGuardian implements Serializable {

	private static final long serialVersionUID = -6684970845319703612L;
	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "target_id")
	private Integer targetId;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "project_id")
	private String projectId;

	public LiveUserGuardian(){

	}

	public LiveUserGuardian(String userId,int targetId,String projectId,Date endTime){
		this.userId=userId;
		this.targetId=targetId;
		this.projectId=projectId;
		this.endTime=endTime;
	}

}

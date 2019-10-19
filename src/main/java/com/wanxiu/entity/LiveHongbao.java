package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_hongbao")
public class LiveHongbao  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private Integer amount;

	private Integer count;

	@Column(name = "create_user")
	private String createUser;

	@Column(name = "create_time")
	private java.util.Date createTime;

	private int status;

	private String showId;

	public LiveHongbao(){

	}

	public LiveHongbao(int amount, int count, String createUser, Date createTime,int status,String showId){
		this.amount=amount;
		this.count=count;
		this.createTime=createTime;
		this.createUser=createUser;
		this.status=status;
		this.showId=showId;
	}

}

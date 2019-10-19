package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "live_moments")
public class LiveMoments  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String content;

	private String images;

	public LiveMoments (){

	}

	public LiveMoments (String content, String images, String createUser, int likeCnt, int commentCnt, int status, Date createTime){
		this.content=content;
		this.images=images;
		this.createUser=createUser;
		this.likeCnt=likeCnt;
		this.commentCnt=commentCnt;
		this.status=status;
		this.createTime=createTime;
	}

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "like_cnt")
	private Integer likeCnt;

	@Column(name = "comment_cnt")
	private Integer commentCnt;

	@Column(name = "create_user")
	private String createUser;

	private Integer status;

}

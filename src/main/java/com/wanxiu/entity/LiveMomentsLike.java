package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_moments_like")
public class LiveMomentsLike  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	private String idol;

	@Column(name = "moments_id")
	private String momentsId;

	public LiveMomentsLike(){

	}
	public LiveMomentsLike(String userId,String idol,String momentsId){
		this.userId=userId;
		this.idol=idol;
		this.momentsId=momentsId;
	}

}

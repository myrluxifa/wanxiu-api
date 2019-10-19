package com.wanxiu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_minivideo")
public class LiveMinivideo  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String title;

	private String url;

	@Column(name = "poster_url")
	private String posterUrl;

	private Integer praise;

	private Integer comment;

	private Integer share;

	private Integer views;

	private Integer status;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "create_user")
	private String createUser;

	@JsonProperty("isMyLove")
	private String remark;

}

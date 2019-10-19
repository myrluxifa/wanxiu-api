package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_banner")
public class LiveBanner  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name="sort_num")
	private Integer sortNum;

	private String title;

	private String url;

	@Column(name = "web_editor")
	private String webEditor;

	private String image;

	private Integer flag;

	private Integer status;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "create_user")
	private String createUser;

	@Column(name = "last_opt_time")
	private java.util.Date lastOptTime;

	@Column(name = "last_opt_user")
	private String lastOptUser;

	private String roomType;
}

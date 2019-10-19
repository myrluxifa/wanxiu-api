package com.wanxiu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_minivideo_comment")
public class LiveMinivideoComment  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "video_id")
	private String videoId;

	private String comment;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "create_user")
	private String createUser;

	@Column(name = "at_user")
	private String atUser;

	private Integer praise;

	@OneToOne
	@JoinColumn(name = "create_user", referencedColumnName = "id", insertable = false, updatable = false)
	private LiveUser user;

}

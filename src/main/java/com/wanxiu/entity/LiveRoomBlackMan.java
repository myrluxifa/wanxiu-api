package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_room_black_man")
public class LiveRoomBlackMan  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private Integer type;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "show_id")
	private Integer showId;

	@Column(name = "start_time")
	private java.util.Date startTime;

	@Column(name = "end_time")
	private java.util.Date endTime;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "create_user")
	private String createUser;

	private String remark;

}

package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_room_type")
public class LiveRoomType  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String name;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "create_user")
	private String createUser;

	private Integer status;

	private Integer order;

}

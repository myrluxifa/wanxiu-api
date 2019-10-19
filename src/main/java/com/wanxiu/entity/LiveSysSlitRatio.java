package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_sys_slit_ratio")
public class LiveSysSlitRatio  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String type;

	private Integer ratio;

	@Column(name = "update_user")
	private String updateUser;

	@Column(name = "update_time")
	private java.util.Date updateTime;

}

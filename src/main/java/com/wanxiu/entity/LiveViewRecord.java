package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_view_record")
public class LiveViewRecord  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "show_id")
	private Integer showId;

	@OneToOne
	@JoinColumn(name = "show_id", referencedColumnName = "show_id", insertable = false, updatable = false)
	private LiveUser streamer;

	@Column(name = "create_time")
	private java.util.Date createTime;

	@Column(name = "create_user")
	private String createUser;

}

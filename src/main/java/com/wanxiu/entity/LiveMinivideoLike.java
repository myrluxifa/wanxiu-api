package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_minivideo_like")
public class LiveMinivideoLike  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "video_id")
	private String videoId;

	@Column(name = "create_time")
	private java.util.Date createTime;

}

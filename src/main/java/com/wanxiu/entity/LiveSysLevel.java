package com.wanxiu.entity;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_sys_level")
public class LiveSysLevel  implements Serializable {

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String name;

	@Column(name = "min_ex")
	private Integer minEx;

	@Column(name = "max_ex")
	private Integer maxEx;

	private Integer sort;

	@Column(name = "image_url")
	private String imageUrl;

}

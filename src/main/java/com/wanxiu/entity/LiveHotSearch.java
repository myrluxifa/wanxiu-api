package com.wanxiu.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_hot_search")
public class LiveHotSearch implements Serializable {

	private static final long serialVersionUID = 6314299500101588353L;

	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	private String content;

	private Integer count;

	@Column(name = "is_admin")
	private Integer isAdmin;

	private Integer status;
}

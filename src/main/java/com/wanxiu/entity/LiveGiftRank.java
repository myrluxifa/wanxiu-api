package com.wanxiu.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "live_gift_rank")
@DynamicInsert
@DynamicUpdate
public class LiveGiftRank implements Serializable {

	private static final long serialVersionUID = -666738696317115196L;
	@Id
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@GeneratedValue(generator="system-uuid")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "show_id")
	private Integer showId;

	@Column(name = "coins")
	private Integer coins;

	@Column(name = "cycle_id")
	private String cycleId;

	@Column(name = "type")
	private String type;

	@Column(name = "user_type")
	private String userType;

	@OneToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id", insertable = false, updatable = false)
	private LiveUser user;
}

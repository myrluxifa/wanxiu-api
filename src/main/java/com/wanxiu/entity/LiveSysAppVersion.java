package com.wanxiu.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="live_sys_app_version")
public class LiveSysAppVersion {

    @Id
    @GenericGenerator(name="system-uuid",strategy="uuid")
    @GeneratedValue(generator="system-uuid")
    private String id;

    private int type;

    private String apkUri;

    private String versionNum;

    private String orderSort;
}

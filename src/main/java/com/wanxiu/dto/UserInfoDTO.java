package com.wanxiu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDTO {
    private String userId;

    private String part;

    private String userName;

    private String nickname;

    private String sex;

    private String age;

    private String birthday;

    private String signature;

    private String showId;

    private String level;

    private String experience;

    private String headPortrait;

    private String background;

    private Integer fans;

    private Integer praise;

    private Integer attentionCnt;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date lastShowTime;

    private String openId;

    private String coins;

}

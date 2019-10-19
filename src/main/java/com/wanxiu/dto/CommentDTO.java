package com.wanxiu.dto;

import lombok.Data;

@Data
public class CommentDTO {

    private String id;

    private String comment;

    private String createUser;

    private String createTime;

    private String callUser;

    private String momentId;

}

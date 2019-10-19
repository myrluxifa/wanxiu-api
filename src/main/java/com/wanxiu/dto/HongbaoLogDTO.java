package com.wanxiu.dto;

import lombok.Data;

@Data
public class HongbaoLogDTO {

    private String nickname;

    private String amount;

    public HongbaoLogDTO() {
    }
    public HongbaoLogDTO(Object[] objects) {
        this.nickname=String.valueOf(objects[0]==null?"":objects[0]);
        this.amount=String.valueOf(objects[1]==null?"":objects[1]);
    }
}

package com.wanxiu.roomservice.pojo.Request;

import lombok.Data;

@Data
public class GetRoomListRoomTypeReq {
    private int cnt = 0;
    private int index = 0;
    private String roomType="";
}

package com.wanxiu.roomservice.pojo.Request;

import lombok.Data;

@Data
public class SetRoomTypeReq {
    private String roomID = "";
    private String roomType="";

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}

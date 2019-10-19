package com.wanxiu.roomservice.pojo.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanxiu.roomservice.pojo.Room;

import java.util.ArrayList;

public class GetRoomListRsp extends BaseRsp {
    @JsonProperty(value = "rooms")
    private ArrayList<Room> rooms;

    @JsonIgnore
    public ArrayList<Room> getList() {
        return rooms;
    }

    public void setList(ArrayList<Room> list) {
        this.rooms = list;
    }
}

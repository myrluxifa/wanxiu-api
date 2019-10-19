package com.wanxiu.roomservice.controller;


import com.wanxiu.roomservice.logic.RoomMgr;
import com.wanxiu.roomservice.pojo.Request.*;
import com.wanxiu.roomservice.pojo.Response.*;
import com.wanxiu.roomservice.pojo.Room;
import com.wanxiu.roomservice.service.RoomService;
import com.wanxiu.roomservice.service.UtilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 双人房间接口
 */
@Controller
@ResponseBody
@RequestMapping("/weapp/double_room")
@Api(tags="双人房间接口")
public class DoubleRoom {
    @Autowired
    RoomService roomService;

    @Autowired
    UtilService utilService;

    @ResponseBody
    @ApiOperation(" 登录接口，校验im签名合法性并派发token，后续请求需要校验token。")
    @RequestMapping(value="login",method = RequestMethod.POST)
    public LoginRsp login(String sdkAppID, String accountType, String userID, String userSig){
        return roomService.login(sdkAppID, accountType, userID, userSig);
    }

    @ResponseBody
    @ApiOperation(" 退出登录接口，清理后台保存的token信息")
    @RequestMapping(value="logout",method = RequestMethod.POST)
    public BaseRsp logout(String userID, String token){
        return roomService.logout(userID, token);
    }

    @ResponseBody
    @ApiOperation(" 获取推流地址接口")
    @RequestMapping("get_push_url")
    public GetPushUrlRsp get_push_url(String userID, String token, @RequestBody GetPushUrlReq req){
        return utilService.getPushUrl(userID, token, req);
    }

    @ResponseBody
    @ApiOperation("获取双人房间列表")
    @RequestMapping(value="get_room_list",method = RequestMethod.POST)
    public GetRoomListRsp get_room_list(String userID, String token, @RequestBody GetRoomListReq req){
        return roomService.getRoomList(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @ApiOperation("获取推流者列表")
    @RequestMapping(value = "get_pushers",method = RequestMethod.POST)
    public Room get_pushers(String userID, String token, @RequestBody GetPushersReq req){
        return roomService.getPushers(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @ApiOperation("创建双人房间")
    @RequestMapping(value = "create_room",method = RequestMethod.POST)
    public CreateRoomRsp create_room(String userID, String token, @RequestBody CreateRoomReq req){
        return roomService.createRoom(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @ApiOperation("销毁双人房间")
    @RequestMapping(value = "destroy_room",method = RequestMethod.POST)
    public BaseRsp destroy_room(String userID, String token, @RequestBody DestroyRoomReq req) {
        return roomService.destroyRoom(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @ApiOperation("增加一个推流者")
    @RequestMapping(value = "add_pusher",method = RequestMethod.POST)
    public BaseRsp add_pusher(String userID, String token, @RequestBody AddPusherReq req) {
        return roomService.addPusher(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @ApiOperation("删除一个推流者")
    @RequestMapping(value = "delete_pusher",method = RequestMethod.POST)
    public BaseRsp delete_pusher(String userID, String token, @RequestBody DeletePusherReq req) {
        return roomService.deletePusher(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }

    @ResponseBody
    @ApiOperation("推流者心跳")
    @RequestMapping(value = "pusher_heartbeat",method = RequestMethod.POST)
    public BaseRsp pusher_heartbeat(String userID, String token, @RequestBody PusherHeartbeatReq req) {
        return roomService.pusherHeartbeat(userID, token, req, RoomMgr.DOUBLE_ROOM);
    }
}

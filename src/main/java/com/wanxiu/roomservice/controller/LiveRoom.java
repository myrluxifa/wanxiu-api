package com.wanxiu.roomservice.controller;

import com.wanxiu.entity.LiveViewRecord;
import com.wanxiu.repository.LiveViewRecordRepository;
import com.wanxiu.roomservice.common.Config;
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

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

/**
 * 直播-连麦 房间接口
 */
@Controller
@ResponseBody
@RequestMapping("/weapp/live_room")
@Api(tags = "直播-连麦 房间接口")
public class LiveRoom {
    @Autowired
    RoomService roomService;

    @Autowired
    UtilService utilService;

    @Autowired
    LiveViewRecordRepository recordRepository;

    @ResponseBody
    @ApiOperation("登录接口，校验im签名合法性并派发token，后续请求需要校验token。")
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public LoginRsp login(String sdkAppID, String accountType, String userID, String userSig){
        return roomService.login(sdkAppID, Config.IM.IM_ACCOUNTTYPE, userID, userSig);
    }

    @ResponseBody
    @ApiOperation("退出登录接口，清理后台保存的token信息")
    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public BaseRsp logout(String userID, String token){
        return roomService.logout(userID, token);
    }

    @ResponseBody
    @ApiOperation("获取推流地址接口")
    @RequestMapping(value = "get_push_url",method = RequestMethod.POST)
    public GetPushUrlRsp get_push_url(String userID, String token){
        return utilService.getPushUrl(userID, token);
    }

    @ResponseBody
    @ApiOperation("获取直播房间列表")
    @RequestMapping(value = "get_room_list",method = RequestMethod.POST)
    public GetRoomListRsp get_room_list(String userID, String token, @RequestBody GetRoomListReq req){
        return roomService.getRoomList(userID, token, req, RoomMgr.LIVE_ROOM);
    }


    @ResponseBody
    @ApiOperation("根据roomtype获取直播房间列表")
    @RequestMapping(value = "get_room_list_roomtype",method = RequestMethod.POST)
    public GetRoomListRsp get_room_list_roomtype(String userID, String token, @RequestBody GetRoomListRoomTypeReq req){
        return roomService.getRoomListRoomType(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("获取指定房间的推流者列表")
    @RequestMapping(value = "get_pushers",method = RequestMethod.POST)
    public Room get_pushers(String userID, String token, @RequestBody GetPushersReq req){
        return roomService.getPushers(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("创建直播房间")
    @RequestMapping(value = "create_room",method = RequestMethod.POST)
    public CreateRoomRsp create_room(String userID, String token, @RequestBody CreateRoomReq req){
        return roomService.createRoom(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("给房间增加类型")
    @RequestMapping(value = "set_room_type",method = RequestMethod.POST)
    public BaseRsp set_room_type(String userID, String token, @RequestBody SetRoomTypeReq req){
        return roomService.setRoomType(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("清除房间后清除类型")
    @RequestMapping(value = "del_room_type",method = RequestMethod.POST)
    public BaseRsp del_room_type(String userID, String token, @RequestBody SetRoomTypeReq req){
        return roomService.delRoomType(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("销毁直播房间")
    @RequestMapping(value = "destroy_room",method = RequestMethod.POST)
    public BaseRsp destroy_room(String userID, String token, @RequestBody DestroyRoomReq req) {
        return roomService.destroyRoom(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("增加一个推流者")
    @RequestMapping(value = "add_pusher",method = RequestMethod.POST)
    public BaseRsp add_pusher(String userID, String token, @RequestBody AddPusherReq req) {
        return roomService.addPusher(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("删除一个推流者")
    @RequestMapping(value = "delete_pusher",method = RequestMethod.POST)
    public BaseRsp delete_pusher(String userID, String token, @RequestBody DeletePusherReq req) {
        return roomService.deletePusher(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("推流者心跳")
    @RequestMapping(value = "pusher_heartbeat",method = RequestMethod.POST)
    public BaseRsp pusher_heartbeat(String userID, String token, @RequestBody PusherHeartbeatReq req) {
        return roomService.pusherHeartbeat(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("连麦混流接口")
    @RequestMapping(value = "merge_stream",method = RequestMethod.POST)
    public MergeStreamRsp merge_stream(String userID, String token, @RequestBody Map map) {
        return utilService.mergeStream(userID, token, map);
    }

    @ResponseBody
    @ApiOperation("获取房间自定义信息，可以用来实现统计房间观众数等功能。")
    @RequestMapping(value = "get_custom_info",method = RequestMethod.POST)
    public GetCustomInfoRsp get_custom_info(String userID, String token, @RequestBody GetCustomInfoReq req) {
        return roomService.getCustomInfo(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation(" 设置房间自定义字段，可以用来实现统计房间观众数等功能。")
    @RequestMapping(value = "set_custom_field",method = RequestMethod.POST)
    public GetCustomInfoRsp set_custom_field(String userID, String token, @RequestBody SetCustomInfoReq req) {
        return roomService.setCustomInfo(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("增加一个观众")
    @RequestMapping(value = "add_audience",method = RequestMethod.POST)
    public BaseRsp add_audience(String userID, String token, @RequestBody AddAudienceReq req) {
        LiveViewRecord record = recordRepository.findByCreateUserAndShowId(userID, Integer.valueOf(req.getRoomID()));
        if (null == record) {
            record = new LiveViewRecord();
            record.setCreateUser(userID);
            record.setShowId(Integer.valueOf(req.getRoomID()));
        }
        record.setCreateTime(Calendar.getInstance().getTime());
        recordRepository.save(record);
        return roomService.addAudience(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("删除一个观众")
    @RequestMapping(value = "delete_audience",method = RequestMethod.POST)
    public BaseRsp delete_audience(String userID, String token, @RequestBody DelAudienceReq req) {
        return roomService.delAudience(userID, token, req, RoomMgr.LIVE_ROOM);
    }

    @ResponseBody
    @ApiOperation("获取观众列表")
    @RequestMapping(value = "get_audiences",method = RequestMethod.POST)
    public GetAudiencesRsp get_audiences(String userID, String token, @RequestBody GetAudiencesReq req) {
        return roomService.getAudiences(userID, token, req, RoomMgr.LIVE_ROOM);
    }
}

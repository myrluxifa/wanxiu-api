package com.wanxiu.roomservice.service;

import com.wanxiu.roomservice.pojo.Request.GetPushUrlReq;
import com.wanxiu.roomservice.pojo.Response.*;

import java.util.Map;

public interface UtilService {
    GetPushUrlRsp getPushUrl(String userID, String token);

    GetPushUrlRsp getPushUrl(String userID, String token, GetPushUrlReq req);

    MergeStreamRsp mergeStream(String userID, String token, Map map);

    GetTestPushUrlRsp getTestPushUrl();

    GetTestRtmpAccUrlRsp getTestRtmpAccUrl();
}

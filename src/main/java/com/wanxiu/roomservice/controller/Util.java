package com.wanxiu.roomservice.controller;


import com.tls.tls_sigature.tls_sigature;
import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.roomservice.common.Config;
import com.wanxiu.roomservice.logic.IMMgr;
import com.wanxiu.roomservice.pojo.Request.GetLoginInfoReq;
import com.wanxiu.roomservice.pojo.Response.GetLoginInfoRsp;
import com.wanxiu.roomservice.pojo.Response.GetTestPushUrlRsp;
import com.wanxiu.roomservice.pojo.Response.GetTestRtmpAccUrlRsp;
import com.wanxiu.roomservice.service.UtilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@ResponseBody
@RequestMapping("/weapp/utils")
@Api(value="util",tags="通用后台协议")
public class Util {
    @Resource
    IMMgr imMgr;

    @Autowired
    UtilService utilService;

    @ResponseBody
    @ApiOperation("获取sdkAppID、accType、userID、userSig信息")
    @RequestMapping(value = "get_login_info",method = RequestMethod.POST)
    public GetLoginInfoRsp get_login_info(@ModelAttribute GetLoginInfoReq req){
        return imMgr.getLoginInfo(req.getUserID());
    }

    @ResponseBody
    @ApiOperation("获取一个随机的推流地址")
    @RequestMapping(value = "get_test_pushurl", method = RequestMethod.POST)
    public GetTestPushUrlRsp get_test_pushurl(){
        return utilService.getTestPushUrl();
    }

    @ResponseBody
    @ApiOperation(" 获取体验低延时播放的地址")
    @RequestMapping(value = "get_test_rtmpaccurl",method = RequestMethod.POST)
    public GetTestRtmpAccUrlRsp get_test_rtmpaccurl(){
        return utilService.getTestRtmpAccUrl();
    }


    @ApiOperation(value = "获得腾讯云通信的用户签名")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户编号",required = true)
    })
    @RequestMapping(value="/getUserSig",method = RequestMethod.POST)
    public ResEntity<String> getUserSig(String userId){

        tls_sigature.GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(Config.IM.IM_SDKAPPID, userId, Config.IM.PRIVATEKEY , 24*3600*180);
        return new ResEntity<String>(Common.RESULT.SUCCESS,result.urlSig);
    }
}

package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.AttentionDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.roomservice.pojo.Response.GetStreamStatusRsp;
import com.wanxiu.service.AttentionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "attentionAPI",tags={"关注"})
@RestController
public class AttentionAPI {

    @Autowired
    private AttentionService attentionService;


    @RequestMapping(value = "/attention",method = RequestMethod.POST)
    @ApiOperation("关注")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="idolId",value="被关注者id",required = true),
            @ApiImplicitParam(paramType = "query", name="userId",value="关注者",required = true)
    })
    public ResEntity attention(String idolId,String userId){
        if(attentionService.attention(idolId,userId)){
            return new ResEntity(Common.RESULT.SUCCESS);
        }else{
            return new ResEntity(Common.RESULT.FAIL);
        }
    }

    @RequestMapping(value = "/cancelAttention",method = RequestMethod.POST)
    @ApiOperation("取消关注")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="idolId",value="被关注者id",required = true),
            @ApiImplicitParam(paramType = "query", name="userId",value="关注者",required = true)
    })
    public ResEntity cancelAttention(String idolId,String userId){
        if(attentionService.cancelAttention(idolId,userId)){
            return new ResEntity(Common.RESULT.SUCCESS);
        }else{
            return new ResEntity(Common.RESULT.FAIL);
        }
    }

    @ApiOperation("关注列表")
    @RequestMapping(value = "/attentionList",method = RequestMethod.POST)
    public ResEntity attentionList(String userId,String page,String pageSize){
        List<AttentionDTO> attentionDTOList=attentionService.getAttentionList(userId,page,pageSize);
        return new ResEntity(Common.RESULT.SUCCESS,attentionDTOList);
    }

    @ApiOperation("粉丝列表")
    @RequestMapping(value = "/fans",method = RequestMethod.POST)
    public ResEntity fans(String userId,String page,String pageSize){
        List<AttentionDTO> attentionDTOList=attentionService.getFansList(userId,page,pageSize);
        return new ResEntity(Common.RESULT.SUCCESS,attentionDTOList);
    }

    @ApiOperation("获取直播状态")
    @RequestMapping(value = "/getStatus",method=RequestMethod.POST)
    public ResEntity getStatus(String roomId){
        GetStreamStatusRsp getStreamStatusRsp =attentionService.getStatus(roomId);
        return new ResEntity(Common.RESULT.SUCCESS,getStreamStatusRsp);
    }

}

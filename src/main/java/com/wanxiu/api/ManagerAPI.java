package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.*;
import com.wanxiu.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(value = "ManagerAPI", tags = {"管理员"})
@RestController
@RequestMapping("api/manager")
public class ManagerAPI {

    @Autowired
    private LiveRoomManagerRepository managerRepository;

    @Autowired
    private LiveRoomBlackManRepository blackManRepository;

    @Autowired
    private LiveUserRepository liveUserRepository;

    @Autowired
    private LiveFansRepository liveFansRepository;

    @Autowired
    private LiveUserGuardianRepository liveUserGuardianRepository;

    @ApiOperation(value = "管理员列表")
    @PostMapping("page")
    public ResEntity<Object> page(Integer showId, Integer page, Integer size) {

        List<LiveRoomManager> managers = managerRepository.findByShowIdOrderByCreateTimeDesc(showId);

        return new ResEntity<>(Common.RESULT.SUCCESS, managers);
    }

    @ApiOperation(value = "查询是否是管理 是否禁言 是否黑名单 返回0黑名单1禁言 -1正常用户 manager 0否1是")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户",required = true),
            @ApiImplicitParam(paramType = "query",name="showId",value="主播",required = true)
    })
    @PostMapping("is2")
    public ResEntity<Object> search2(Integer showId, String userId) {

        LiveRoomBlackMan blackMan = blackManRepository.findByUserIdAndShowId(userId, showId);

        Map<String, Long> result = new HashMap<>();
        if (blackMan == null) {
            result.put("state", -1l);
            result.put("endTime", 0l);
        } else {
            result.put("state", blackMan.getType().longValue());
            if (blackMan.getType() == 1) {
                if (blackMan.getEndTime().after(new Date())) {
                    result.put("state", 1l);
                }else {
                    result.put("state", -1l);
                }
            }
            if (null != blackMan.getEndTime()) {
                long tt = blackMan.getEndTime().getTime() - Calendar.getInstance().getTimeInMillis();
                result.put("endTime", tt);
                if (tt <= 0) {
                    result.put("endTime", 0l);
                    result.put("state", -1l);
                }
            }
            else
                result.put("endTime", 0l);
        }


        LiveRoomManager manager = managerRepository.findByShowIdAndUserId(showId, userId);
        if (manager == null) {
            result.put("manager", 0l);
        } else {
            result.put("manager", 1l);
        }

        LiveUser liveUser=liveUserRepository.findByShowId(Integer.valueOf(showId));
        Optional<LiveFans> liveFansOptional=liveFansRepository.findByFansAndIdol(userId,liveUser.getId());

        result.put("attention", 0l);
        if(liveFansOptional.isPresent()){
            result.put("attention", 1l);
        }

        result.put("guard", 0l);
        Optional<LiveUserGuardian> optionalLiveUserGuardian=liveUserGuardianRepository.findByUserIdAndTargetIdAndEndTimeGreaterThan(userId,Integer.valueOf(showId),new Date());
        String ifGuard="false";
        String guardType="";
        if(optionalLiveUserGuardian.isPresent()){
            ifGuard="true";
            if(optionalLiveUserGuardian.get().getProjectId().equals("1")){
                result.put("guard", 1l);
            }else{
                result.put("guard", 2l);
            }
        }
        return  new ResEntity<>(Common.RESULT.SUCCESS, result);
    }

    @ApiOperation(value = "查询是否是管理 是否禁言 是否黑名单 返回0黑名单1禁言 -1正常用户 manager 0否1是")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户",required = true),
            @ApiImplicitParam(paramType = "query",name="showId",value="主播",required = true)
    })
    @PostMapping("is")
    public ResEntity<Object> search(Integer showId, String userId) {

        LiveRoomBlackMan blackMan = blackManRepository.findByUserIdAndShowId(userId, showId);

        Map<String, Long> result = new HashMap<>();
        if (blackMan == null) {
            result.put("state", -1l);
            result.put("endTime", 0l);
        } else {
            result.put("state", blackMan.getType().longValue());
            if (blackMan.getType() == 1) {
                if (blackMan.getEndTime().after(new Date())) {
                    result.put("state", 1l);
                }else {
                    result.put("state", -1l);
                }
            }
            if (null != blackMan.getEndTime()) {
                long tt = blackMan.getEndTime().getTime() - Calendar.getInstance().getTimeInMillis();
                result.put("endTime", tt);
                if (tt <= 0) {
                    result.put("endTime", 0l);
                    result.put("state", -1l);
                }
            }
            else
                result.put("endTime", 0l);
        }


        LiveRoomManager manager = managerRepository.findByShowIdAndUserId(showId, userId);
        if (manager == null) {
            result.put("manager", 0l);
        } else {
            result.put("manager", 1l);
        }
        return  new ResEntity<>(Common.RESULT.SUCCESS, result);
    }

    @ApiOperation(value = "设置管理员")
    @PostMapping("set")
    public ResEntity<Object> set(Integer showId, String targetId, String userId) {

        LiveRoomManager manager = managerRepository.findByShowIdAndUserId(showId, targetId);
        if (manager == null) {
            manager = new LiveRoomManager();
            manager.setCreateTime(Calendar.getInstance().getTime());
            manager.setCreateUser(userId);
            manager.setShowId(showId);
            manager.setUserId(targetId);
            managerRepository.save(manager);
            return new ResEntity<Object>(Common.RESULT.SUCCESS, 1);
        } else {
            managerRepository.delete(manager);
            return new ResEntity<>(Common.RESULT.SUCCESS, 0);
        }

    }
}

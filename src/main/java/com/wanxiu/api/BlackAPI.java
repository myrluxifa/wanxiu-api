package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveRoomBlackMan;
import com.wanxiu.repository.LiveRoomBlackManRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(value = "BlackAPI", tags = {"黑名单"})
@RestController
@RequestMapping("api/black")
public class BlackAPI {

    @Autowired
    private LiveRoomBlackManRepository blackManRepository;

    @ApiOperation(value = "查询禁言/黑名单 返回0黑名单1禁言 -1正常用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户",required = true),
            @ApiImplicitParam(paramType = "query",name="showId",value="主播",required = true)
    })
    @PostMapping("find")
    public ResEntity<Object> find(String userId, Integer showId) {

        LiveRoomBlackMan blackMan = blackManRepository.findByUserIdAndShowId(userId, showId);

        Map<String, Long> result = new HashMap<>();
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

        return new ResEntity<Object>(Common.RESULT.SUCCESS, result);
    }

    @ApiOperation(value = "设置禁言/黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="禁言人",required = true),
            @ApiImplicitParam(paramType = "query",name="showId",value="主播",required = true),
            @ApiImplicitParam(paramType = "query",name="hours",value="小时数",required = true),
            @ApiImplicitParam(paramType = "query",name="operatorId",value="操作人",required = true)
    })
    @PostMapping("set")
    public ResEntity<Object> save(String userId, Integer showId, Integer hours, String operatorId) {

        Date startTime = null;
        Date endTime = null;
        int type = 0;
        if (hours > 0) {
            type = 1;
            Calendar now = Calendar.getInstance();
            startTime = now.getTime();
            now.add(Calendar.HOUR_OF_DAY, hours);
            endTime = now.getTime();
        }

        LiveRoomBlackMan blackMan = blackManRepository.findByUserIdAndShowId(userId, showId);
        if (null == blackMan) {
            blackMan = new LiveRoomBlackMan();
            blackMan.setEndTime(endTime);
            blackMan.setShowId(showId);
            blackMan.setStartTime(startTime);
            blackMan.setType(type);
            blackMan.setUserId(userId);
        } else if (blackMan.getType() == 1) {
            blackMan.setStartTime(startTime);
            blackMan.setEndTime(endTime);
        }

        if (hours <= 0) {
            blackMan.setType(0);
        }

        blackMan.setCreateTime(Calendar.getInstance().getTime());
        blackMan.setCreateUser(operatorId);

        blackManRepository.save(blackMan);

        return new ResEntity<>(Common.RESULT.SUCCESS);
    }
}

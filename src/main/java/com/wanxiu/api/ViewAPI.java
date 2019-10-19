package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveViewRecord;
import com.wanxiu.repository.LiveViewRecordRepository;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"观看"}, value = "ViewAPI")
@RestController
@RequestMapping("api/view")
public class ViewAPI {

    @Autowired
    LiveViewRecordRepository recordRepository;

    @ApiOperation(value = "观看记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="page",value="1",required = true),
            @ApiImplicitParam(paramType = "query",name="size",value="20",required = true),
            @ApiImplicitParam(paramType = "query",name="userId",value="10086",required = true)
    })
    @PostMapping("log/page")
    public ResEntity<Object> page(Integer page, Integer size, String userId) {

        List<LiveViewRecord> records = recordRepository.findByCreateUserOrderByCreateTimeDesc(PagePlugin.pagePlugin(page, size), userId);

        return new ResEntity<>(Common.RESULT.SUCCESS, records);
    }

    @ApiOperation(value = "清空观看记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="10086",required = true)
    })
    @PostMapping("log/del")
    public ResEntity<Object> clean(String userId) {

        recordRepository.deleteByCreateUser(userId);

        return new ResEntity<>(Common.RESULT.SUCCESS);
    }
}

package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveFans;
import com.wanxiu.repository.LiveFansRepository;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "FansAPI", tags = {"粉丝"})
@RestController
@RequestMapping("api/fans")
public class FansAPI {

    @Autowired
    LiveFansRepository fansRepository;

    @ApiOperation(value = "粉丝列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "1", required = true),
            @ApiImplicitParam(paramType = "query", name = "size", value = "20", required = true),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "10086", required = true)
    })
    @PostMapping("page")
    public ResEntity<Object> page(Integer page, Integer size, String userId) {

        List<LiveFans> fans = fansRepository.findByIdol(
                PagePlugin.pagePluginSort(page, size, Sort.Direction.DESC, "createTime"),
                userId);

        return new ResEntity<>(Common.RESULT.SUCCESS, fans);
    }
}

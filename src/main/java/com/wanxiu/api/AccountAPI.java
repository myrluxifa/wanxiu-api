package com.wanxiu.api;

import com.wanxiu.common.CoinsLogParam;
import com.wanxiu.common.Common;
import com.wanxiu.dto.ClientCoinsLogDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveCoinsLog;
import com.wanxiu.mapper.CoinsLogMapper;
import com.wanxiu.repository.LiveAccountRepository;
import com.wanxiu.repository.LiveCoinsLogRepository;
import com.wanxiu.service.AccountService;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "AccountAPI", tags = {"账户"})
@RestController
@RequestMapping("api/account")
public class AccountAPI {

    @Autowired
    LiveCoinsLogRepository coinsLogRepository;

    @Autowired
    CoinsLogMapper coinsLogMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LiveAccountRepository accountRepository;

    @ApiOperation(value = "账户流水")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="page",value="1",required = true),
            @ApiImplicitParam(paramType = "query",name="size",value="20",required = true),
            @ApiImplicitParam(paramType = "query",name="userId",value="10086",required = true),
            @ApiImplicitParam(paramType = "query",name="type",value="I-收入 E-支出 W-提现 R-充值",required = true)
    })
    @PostMapping("log/page")
    public ResEntity accountLog(Integer page, Integer size, String userId, String type) {

        List<LiveCoinsLog> logs;
        if("ALL".equals(type)) {
            logs = coinsLogRepository.findByUserId(
                    PagePlugin.pagePluginSort(page, size, Sort.Direction.DESC, "createTime"),
                    userId);

        } else {
            logs = coinsLogRepository.findByUserIdAndTypeIn(
                    PagePlugin.pagePluginSort(page, size, Sort.Direction.DESC, "createTime"),
                    userId, getTypes(type));

        }
        return new ResEntity(Common.RESULT.SUCCESS, logs);

    }

    @ApiOperation(value = "账户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="10086",required = true)
    })
    @PostMapping
    public ResEntity<Object> account(String userId) {

        return new ResEntity<Object>(Common.RESULT.SUCCESS, accountRepository.findByUserId(userId));
    }

    @ApiOperation(value="客户端秀币日志")
    @RequestMapping(value = "clientCoinsLog",method = RequestMethod.POST)
    public ResEntity clientCoinsLog(String userId,Integer page, Integer size){
        List<LiveCoinsLog> liveCoinsLogs =coinsLogRepository.findByUserIdOrderByCreateTimeDesc(userId,PagePlugin.pagePlugin(page, size));
        List<ClientCoinsLogDTO> logDTOS=new ArrayList<>();
        for(LiveCoinsLog log :liveCoinsLogs){

            logDTOS.add(  coinsLogMapper.getClientCoinsDTO(
                    String.valueOf(log.getCoins()),
                    log.getType(),
                    String.valueOf(log.getCreateTime().getTime())
            ));
        }
        return new ResEntity(Common.RESULT.SUCCESS,logDTOS);
    }


    public static List<String> getTypes(String type) {
        List<String> res = new ArrayList<>();
        switch (type) {
            case "I":
                res.add(CoinsLogParam.TYPE_INCOME);
                res.add(CoinsLogParam.TYPE_RECHARGE);
                res.add(CoinsLogParam.TYPE_INCOME_GIFT);
                res.add(CoinsLogParam.TYPE_INCOME_MANAGER);
                break;
            case "E":
                res.add(CoinsLogParam.TYPE_EXPEND);
                res.add(CoinsLogParam.TYPE_EXPEND_GIFT);
                res.add(CoinsLogParam.TYPE_EXPEND_MANAGER);
                break;
            case "W":
                res.add(CoinsLogParam.TYPE_EXPEND_WITHDRAW);
                break;
            case "R":
                res.add(CoinsLogParam.TYPE_RECHARGE);
                break;
            default:
                ;
        }
        return res;
    }

//    public ResEntity queryCoin(String userId){
//
//    }
}

package com.wanxiu.api;

import com.wanxiu.common.Common;
import com.wanxiu.dto.AnchorBalanceDTO;
import com.wanxiu.dto.AnchorInfoDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveAccount;
import com.wanxiu.entity.LiveCashWithdrawal;
import com.wanxiu.repository.LiveAccountRepository;
import com.wanxiu.repository.LiveCashWithdrawalRepository;
import com.wanxiu.service.AccountService;
import com.wanxiu.service.AnchorUserService;
import com.wanxiu.service.LiveUserService;
import com.wanxiu.util.PagePlugin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(value = "anchorUserAPI",tags={"主播用户"})

@RestController
public class AnchorUserAPI {
    @Autowired
    private AnchorUserService anchorUserService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LiveUserService liveUserService;

    @Autowired
    private LiveCashWithdrawalRepository liveCashWithdrawalRepository;

    @Autowired
    private LiveAccountRepository accountRepository;

    @RequestMapping(value="/anchorLogin",method = RequestMethod.POST)
    @ApiOperation(value = "主播登录or注册")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="phone",value="电话号",required = true)
    })
    public ResEntity<AnchorInfoDTO> anchorLogin(String phone){
        boolean ifRegister=liveUserService.check(phone);
        if(ifRegister==true){
            AnchorInfoDTO anchorInfoDTO = anchorUserService.anchorLogin(phone);
            return new ResEntity<AnchorInfoDTO>(Common.RESULT.SUCCESS,anchorInfoDTO);
        }else{
            AnchorInfoDTO anchorInfoDTO=anchorUserService.register(phone);
            return new ResEntity<AnchorInfoDTO>(Common.RESULT.SUCCESS,anchorInfoDTO);
        }
    }

    @ApiOperation(value = "主播余额信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户编号",required = true)
    })
    @RequestMapping(value = "/getAnchorBalance",method = RequestMethod.POST)
    public ResEntity getAnchorBalance(String userId){
        AnchorBalanceDTO anchorBalance =accountService.getAnchorBalance(userId);
        if(anchorBalance==null){
            return new ResEntity(Common.RESULT.FAIL);
        }
        return new ResEntity(Common.RESULT.SUCCESS,anchorBalance);
    }

    @ApiOperation(value = "主播提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户编号",required = true),
            @ApiImplicitParam(paramType = "query",name="coin",value="秀币",required = true)
    })
    @RequestMapping(value = "/applyForWidthdraw",method = RequestMethod.POST)
    public ResEntity applyForWithdraw(String userId,String coin){
        return accountService.applyForWithdraw(userId,coin);
    }

    @ApiOperation(value = "主播提现申请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="userId",value="用户编号",required = true),
            @ApiImplicitParam(paramType = "query",name="page",value="页",required = true),
            @ApiImplicitParam(paramType = "query",name="size",value="页",required = true)
    })
    @RequestMapping(value = "/api/user/withdraw/log",method = RequestMethod.POST)
    public ResEntity logForWithdraw(String userId, Integer page, Integer size){

        List<LiveCashWithdrawal> records = liveCashWithdrawalRepository.findByUserIdOrderByCreateTimeDesc(userId, PagePlugin.pagePlugin(page, size));
        Optional<LiveAccount> acctOpt = accountRepository.findByUserId(userId);

        return new ResEntity(Common.RESULT.SUCCESS, acctOpt.get().getCoins().toString(), records);
    }
}

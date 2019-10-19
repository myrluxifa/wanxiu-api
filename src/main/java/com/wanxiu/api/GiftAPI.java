package com.wanxiu.api;

import com.wanxiu.api.runable.LootHongBao;
import com.wanxiu.common.AccountParam;
import com.wanxiu.common.Common;
import com.wanxiu.common.ServiceRes;
import com.wanxiu.dto.GiftByTypeDTO;
import com.wanxiu.dto.GiftDTO;
import com.wanxiu.dto.GuardianDTO;
import com.wanxiu.dto.LootHongBaoDTO;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveHongbao;
import com.wanxiu.entity.LiveHongbaoLog;
import com.wanxiu.entity.LiveOrder;
import com.wanxiu.repository.LiveUserRepository;
import com.wanxiu.service.AccountService;
import com.wanxiu.service.GiftService;
import com.wanxiu.service.LiveUserService;
import com.wanxiu.service.PayService;
import com.wanxiu.util.HongBaoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

@Api(value = "giftAPI",tags={"礼物"})
@RestController
public class GiftAPI {

    @Autowired
    private GiftService giftService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LiveUserService liveUserService;

    @Autowired
    private PayService payService;

    @ApiOperation(value="购买礼物")
    @PostMapping("/api/gift/buy")
    public ResEntity<Object> buyGift(String userId, Integer showId, String giftId, Integer count) {
        ServiceRes<Object> res = giftService.buyGift(userId, showId, giftId, count,false);
        if (res.isFail()) {
            return new ResEntity<Object>(res.getCode()+"", res.getMessage());
        } else {
            return new ResEntity<>(Common.RESULT.SUCCESS);
        }
    }

    @ApiOperation(value="点歌器购买礼物已支付")
    @PostMapping("/api/gift/buyGiftPrivateRoom")
    public ResEntity<Object> buyGiftPrivateRoom(String userId, Integer showId, String giftId, Integer count,String orderId) {
        //这里要写校验订单接口
//        LiveOrder liveOrder =payService.findById(orderId);
//        if(liveOrder==null){
//            return new ResEntity<>(Common.RESULT.ORDER_404);
//        }
//        if(liveOrder.getPayState()==2){
            ServiceRes<Object> res = giftService.buyGift(userId, showId, giftId, count,true);
            if (res.isFail()) {
                return new ResEntity<Object>(res.getCode()+"", res.getMessage());
            } else {
                return new ResEntity<>(Common.RESULT.SUCCESS);
            }
//        }else{
//            return new ResEntity<>(Common.RESULT.ORDER_UNPAY);
//        }

    }

    @ApiOperation(value="获得礼物列表")
    @RequestMapping(value = "/getGiftList",method = RequestMethod.POST)
    public ResEntity getGiftList(){
        List<GiftDTO> giftDTOList=giftService.getGiftList();
        return new ResEntity(Common.RESULT.SUCCESS,giftDTOList);
    }

    @ApiOperation(value="获得礼物列表带标签")
    @RequestMapping(value = "/getGiftListByType",method = RequestMethod.POST)
    public ResEntity getGiftListByType(){
        Map<Integer,String> typeMap=new HashMap<Integer, String>(){
            {
                put(1,"精品");
                //put(2,"守护");
            }
        };

        List<GiftByTypeDTO> giftByTypeDTOList=new ArrayList<>();
        for(Integer type: typeMap.keySet()){

            giftByTypeDTOList.add(new GiftByTypeDTO(typeMap.get(type),giftService.getGiftByTypeList(type)));
        }
        return new ResEntity(Common.RESULT.SUCCESS,giftByTypeDTOList);
    }

    @ApiOperation(value="获得守护列表")
    @RequestMapping(value = "/getGuardianList",method = RequestMethod.POST)
    public ResEntity getGuardianList(){
        List<GuardianDTO> guardianList=giftService.getGuardianList();
        return new ResEntity(Common.RESULT.SUCCESS,guardianList);
    }



    //红包
    private  ConcurrentHashMap<String, Vector<Integer>> hongbaoMap=new ConcurrentHashMap<>();

    //已抢红包用户集合
    private ConcurrentHashMap<String,Vector<String>> userMap=new ConcurrentHashMap<>();


    @ApiOperation(value = "红包日志"
    )
    @RequestMapping(value = "getHongBaoList",method = RequestMethod.POST)
    public ResEntity getHongBaoList(String hongbaoId){
       return new ResEntity(Common.RESULT.SUCCESS, giftService.getHongBaoList(hongbaoId));
    }


    @ApiOperation(value = "发红包")
    @RequestMapping(value = "/sendHongBao",method = RequestMethod.POST)
    public ResEntity sendHongBao(String totalAmount,String count,String userId,String showId){
        String accountId=accountService.getAccountId(userId);
        if(StringUtils.isBlank(accountId)){
            return new ResEntity(Common.RESULT.ACCOUNT_404);//账户未找到
        }

        int _totalAmount=Integer.valueOf(totalAmount);
        int _count=Integer.valueOf(count);

        if(_totalAmount<100){
            return new ResEntity(Common.RESULT.HONGBAO_TOTALAMOUNNT_ERROR);
        }

        if(_count<1){
            return new ResEntity(Common.RESULT.HONGBAO_COUNT_ERROR);
        }

//
//
        ServiceRes res =accountService.change(accountId,userId,_totalAmount, AccountParam.CHANGE_TYPE_EXPEND_HONGBAO);

        if(res.isFail()){
            return new ResEntity(Common.RESULT.FAIL,res.getMessage());
        }

        LiveHongbao liveHongbao=giftService.saveHongBao(new LiveHongbao(_totalAmount,_count,userId,new Date(),0,showId));
        Vector vector =HongBaoUtil.loadHongBao(_totalAmount,_count);
        hongbaoMap.put(liveHongbao.getId(),vector);
        return new ResEntity(Common.RESULT.SUCCESS, liveHongbao.getId());
    }

    @ApiOperation(value = "抢红包")
    @RequestMapping(value = "/lootHongBao",method = RequestMethod.POST)
    public ResEntity lootHongBao(String userId,String hongbaoKey){

        String accountId=accountService.getAccountId(userId);
        if(StringUtils.isBlank(accountId)){
            return new ResEntity(Common.RESULT.ACCOUNT_404);//账户未找到
        }

        if(!hongbaoMap.containsKey(hongbaoKey)){
            return new ResEntity(Common.RESULT.HONGBAO_404);//红包不存在
        }

        Vector<String> userVector=userMap.get(hongbaoKey);
        if(userVector!=null&&userVector.contains(userId)){
            return new ResEntity(Common.RESULT.HONGBAO_409);//已抢过红包
        }


        LootHongBao lootHongBao=new LootHongBao(hongbaoKey,hongbaoMap);

        FutureTask<Integer> f=new FutureTask<>(lootHongBao);
        new Thread(f).start();
        try{
            int amount=f.get();
            if(amount>0){
                giftService.saveHongBaoLog(new LiveHongbaoLog(userId,amount,hongbaoKey,new Date()));//记录红包日志
                if(userVector==null) userVector=new Vector<>();
                userVector.add(userId);
                userMap.put(hongbaoKey,userVector);
                accountService.change(accountId,userId,amount, AccountParam.CHANGE_TYPE_FROM_HONGBAO);//账户更新
                return new ResEntity(Common.RESULT.SUCCESS,new LootHongBaoDTO(String.valueOf(amount),"1"));
            }else{
                return new ResEntity(Common.RESULT.SUCCESS,new LootHongBaoDTO(String.valueOf(0),"-1"));
            }

        }catch (Exception e){
            System.out.print(e.getMessage());
            return new ResEntity(Common.RESULT.FAIL);
        }
    }



}

package com.wanxiu.service.impl;

import com.wanxiu.common.AccountParam;
import com.wanxiu.common.SlitMoneyParam;
import com.wanxiu.dto.wechat.WxRechargeDto;
import com.wanxiu.entity.*;
import com.wanxiu.repository.AliPayNotifyLogRepository;
import com.wanxiu.repository.LiveAccountRepository;
import com.wanxiu.repository.LiveOrderRepository;
import com.wanxiu.repository.WxPayNotifyLogRepository;
import com.wanxiu.service.AccountService;
import com.wanxiu.service.PayService;
import com.wanxiu.service.SlitMoneyService;
import com.wanxiu.util.WeixinPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class PayServiceImpl implements PayService {

    @Autowired
    private LiveOrderRepository liveOrderRepository;

    @Autowired
    private LiveAccountRepository liveAccountRepository;

    @Autowired
    private AliPayNotifyLogRepository aliPayNotifyLogRepository;

    @Autowired
    private WxPayNotifyLogRepository wxPayNotifyLogRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SlitMoneyService slitMoneyService;


    private static final Logger log = LoggerFactory.getLogger(PayServiceImpl.class);

    public LiveOrder findById(String id){
        Optional<LiveOrder> liveOrder=liveOrderRepository.findById(id);
        if(liveOrder.isPresent()){
            return liveOrder.get();
        }else{
            return null;
        }
    }

    public WxRechargeDto getwxPayDto(String appId, String mchId, String prepayid, String noncestr, String timestamp) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", appId);
        parameterMap.put("partnerid", mchId);
        parameterMap.put("prepayid", prepayid);
        parameterMap.put("package","Sign=WXPay");
        parameterMap.put("noncestr", noncestr);
        parameterMap.put("timestamp", timestamp);
        String sign = WeixinPayUtil.createSign(parameterMap);
        return new WxRechargeDto(mchId,prepayid,noncestr,timestamp,sign,"Sign=WXPay");
    }

    @Override
    public LiveOrder saveOrder(LiveOrder payOrder) {
        // TODO Auto-generated method stub
        return liveOrderRepository.save(payOrder);
    }


//    @Transactional
//    public String yuePay(String userId,int coin,String ip,String orderId,String method) {
//        //先取出用户余额
//        //LiveUser user=userLoginDao.findById(Integer.valueOf(loginId));
//        Optional<LiveAccount> accountOptional=liveAccountRepository.findByUserId(userId);
//
//
//        if(accountOptional.isPresent()){
//            LiveAccount account=accountOptional.get();
//            if(account.getCoins().compareTo(coin)==-1){
//                return "-1";
//            }
//
//            //取出订单
//            Optional<LiveOrder> optionalLiveOrder=liveOrderRepository.findById(orderId);
//            if(optionalLiveOrder.isPresent()){
//                LiveOrder order = optionalLiveOrder.get();
//                if(order.getPayState()==2) {
//                    return "-2";
//                }
//                //逻辑代码
//                order.setPayState(2);
//                liveOrderRepository.save(order);
//                account.setCoins(account.getCoins()-_pay);
//                liveAccountRepository.save(account);
//                return "0";
//            }else{
//                return "-3";
//            }
//        }else{
//            return "-4";
//        }
//    }

    @Override
    public String updateOrderStatus(String orderId, int status) {
        Optional<LiveOrder> optionalLiveOrder=liveOrderRepository.findById(orderId);
        if(optionalLiveOrder.isPresent()){
            LiveOrder liveOrder=optionalLiveOrder.get();
            liveOrder.setPayState(status);
            liveOrderRepository.save(liveOrder);
            return "0";
        }else{
            return "-1";
        }
    }

    @Override
    public void aliNotifyLogSave(Map<String, String> params) {
        aliPayNotifyLogRepository.save(new AliPayNotifyLog(params));
    }

    @Override
    public void wxNotifyLogSave(Map params) {
        wxPayNotifyLogRepository.save(new WxPayNotifyLog(params));
    }


    public void  finishOrder(String orderId,int status){
        Optional<LiveOrder> optionalLiveOrder=liveOrderRepository.findById(orderId);
        LiveOrder liveOrder = new LiveOrder();
        LiveAccount liveAccount=new LiveAccount();



        if(optionalLiveOrder.isPresent()){
            liveOrder=optionalLiveOrder.get();
            Optional<LiveAccount> optionalLiveAccount =liveAccountRepository.findByUserId(liveOrder.getUserId());
            if(optionalLiveAccount.isPresent()){
                liveAccount=optionalLiveAccount.get();
                accountService.change(liveAccount.getId(),liveOrder.getUserId(),liveOrder.getCoin(), AccountParam.CHANGE_TYPE_RECHARGE);
            }
        }

        if(liveOrder.getPayState()==1){
            log.info("准备开始分账");
            slitMoneyService.slitMoney(SlitMoneyParam.SLIT_TYPE_PLATFORM,liveOrder.getCoin(),liveOrder.getPay(),SlitMoneyParam.USE_TYPE_RECHARGE,liveOrder.getUserId(),"");
        }

        liveOrder.setPayState(status);
        liveOrderRepository.save(liveOrder);


    }
}

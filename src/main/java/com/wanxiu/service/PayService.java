package com.wanxiu.service;

import com.wanxiu.dto.wechat.WxRechargeDto;
import com.wanxiu.entity.LiveOrder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PayService {

    LiveOrder findById(String id);

    WxRechargeDto getwxPayDto(String appId, String mchId, String prepayid, String noncestr, String timestamp);

    LiveOrder saveOrder(LiveOrder payOrder);

//  String yuePay(String userId,String pay,String ip,String orderId,String method);

    String updateOrderStatus(String orderId,int status);

    void aliNotifyLogSave(Map<String,String> params);

    void wxNotifyLogSave(Map params);

    void finishOrder(String orderId,int status);

}

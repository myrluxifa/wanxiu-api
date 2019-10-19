package com.wanxiu.controller;

import com.wanxiu.common.Common;
import com.wanxiu.config.CustomConfig;
import com.wanxiu.config.WechatConfig;
import com.wanxiu.dto.PayDto;
import com.wanxiu.dto.res.ResEntity;
import com.wanxiu.entity.LiveOrder;
import com.wanxiu.service.AccountService;
import com.wanxiu.service.GiftService;
import com.wanxiu.service.PayService;
import com.wanxiu.util.WeixinPayUtil;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Controller
@RequestMapping("/wxController")
public class WxController {

    @Autowired
    private PayService payService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private GiftService giftService;


    private static final Logger log = LoggerFactory.getLogger(WxController.class);

    @SuppressWarnings("unchecked")
    @RequestMapping("/rechargeNotify")
    public void rechargeNotify(HttpServletRequest request, HttpServletResponse response){
        InputStream inStream;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            log.info("回调方法");
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
            Map<Object, Object> map;
            try {
                map = WeixinPayUtil.doXMLParse(result);
                log.info("================================================================");
                if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
                    log.info("================================================================验签开始");
                    SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
                    String sign = (String) map.get("sign");
                    for (Object keyValue : map.keySet()) {
                        if(!keyValue.toString().equals("sign")){
                            parameterMap.put(keyValue.toString(), map.get(keyValue));
                        }
                    }
                    String createSign = WeixinPayUtil.createSign(parameterMap);
                    if(createSign.equals(sign)){

                        payService.finishOrder(String.valueOf(map.get("out_trade_no")),2);
                        payService.wxNotifyLogSave(map);

                        response.getWriter().write(WeixinPayUtil.setXML("SUCCESS", "OK"));
                    }else{
                        response.getWriter().write(WeixinPayUtil.setXML("FAIL", "验签失败"));
                    }

                }else {
                    response.getWriter().write(WeixinPayUtil.setXML("FAIL", "验签失败"));
                }
            } catch (JDOMException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Autowired
    private WechatConfig weixin;

    @Autowired
    private CustomConfig customConfig;

    @RequestMapping("/nativePayResUrl")
    public void nativePayResUrl(HttpServletRequest request, HttpServletResponse response){

        log.info("native支付回调");
        InputStream inStream;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
            Map<Object, Object> map;
            try {
                log.info(result);
                map = WeixinPayUtil.doXMLParse(result);
                log.info("================================================================");
                String productId=map.get("product_id").toString();
                String openid=map.get("openid").toString();
                String body="万秀直播-充值";
                LiveOrder liveOrder =payService.findById(productId);
                BigDecimal pay =liveOrder.getPay();
                String wx_notify=customConfig.getUrl()+"/wxController/rechargeNotify";

                SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
                parameterMap.put("appid", weixin.getAppid());
                parameterMap.put("openid",openid);
                parameterMap.put("mch_id",weixin.getMachId());
                parameterMap.put("nonce_str",WeixinPayUtil.getRandomString(32));
                parameterMap.put("body",body);
                parameterMap.put("out_trade_no",productId);
                parameterMap.put("total_fee",String.valueOf(pay.multiply(new BigDecimal(100)).intValue()));
                parameterMap.put("spbill_create_ip","152.136.34.178");
                parameterMap.put("notify_url",wx_notify);
                parameterMap.put("trade_type","NATIVE");
                parameterMap.put("product_id",productId);

                String sign = WeixinPayUtil.createSign(parameterMap);
                parameterMap.put("sign", sign);
                String requestXML = WeixinPayUtil.getRequestXml(parameterMap);
                log.info(requestXML);

                String resultPay = WeixinPayUtil.doRefund(weixin.getMachId(),
                        "https://api.mch.weixin.qq.com/pay/unifiedorder",
                        requestXML);
                log.info(resultPay);
                Map<String, String> mapPay = null;
                try {
                    mapPay = WeixinPayUtil.doXMLParse(resultPay);
                    if(mapPay.get("return_code").equals("SUCCESS")){

                        log.info(mapPay.get("prepay_id").toString());

                        SortedMap<String, Object> responseMap = new TreeMap<String, Object>();
                        responseMap.put("return_code","SUCCESS");
                        responseMap.put("appid",weixin.getAppid());
                        responseMap.put("mch_id",weixin.getMachId());
                        responseMap.put("nonce_str",WeixinPayUtil.getRandomString(32));
                        responseMap.put("prepay_id",mapPay.get("prepay_id").toString());
                        responseMap.put("result_code","SUCCESS");

                        String responseSign = WeixinPayUtil.createSign(responseMap);
                        responseMap.put("sign", responseSign);
                        String responseXML = WeixinPayUtil.getRequestXml(responseMap);
                        response.getWriter().write(responseXML);
                    }else {
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

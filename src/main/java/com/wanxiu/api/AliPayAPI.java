package com.wanxiu.api;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.wanxiu.common.Common;
import com.wanxiu.config.AliPayConfig;
import com.wanxiu.dto.res.ResEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliPayAPI {

    @Autowired
    private AliPayConfig aliPayConfig;

    private Logger log= LoggerFactory.getLogger(AliPayAPI.class);

    @RequestMapping(value = "/alipay",method = RequestMethod.POST)
    public ResEntity aliPay(String userId,String pay,String ip,String orderId,String notify,String body) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), "json", "utf-8",
                aliPayConfig.getAliPublicKey(), "RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject("万秀直播");
        model.setOutTradeNo(orderId);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(pay);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(notify);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            // System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            log.info(response.getBody());
            return new ResEntity(Common.RESULT.SUCCESS,response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return new ResEntity(Common.RESULT.FAIL);
        }
    }
}

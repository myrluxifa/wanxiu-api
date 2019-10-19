package com.wanxiu.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.wanxiu.api.AliPayAPI;
import com.wanxiu.config.AliPayConfig;
import com.wanxiu.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/aliController")
public class AliController {

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private PayService payService;

    private Logger log= LoggerFactory.getLogger(AliController.class);

    @RequestMapping("/rechargNotify")
    public void rechargNotify(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params,aliPayConfig.getAliPublicKey() , "utf-8","RSA2");
            if(flag==true){
                //逻辑代码

                payService.finishOrder(params.get("out_trade_no"),2);
                payService.aliNotifyLogSave(params);
            }else{

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}

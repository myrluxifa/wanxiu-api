package com.wanxiu.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

public class SMSUtil {

   private static Logger logger= LoggerFactory.getLogger(SMSUtil.class);

    public static void sendSMS(int appid,String appkey,String phoneNumber,String code){
        try {
            int templateId=279944;
            String[] params = {code};//数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            String smsSign="万秀荣耀餐饮娱乐中心";
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86",phoneNumber,
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
           // SmsSingleSenderResult result=ssender.send(0,"86",phoneNumber,"【万秀荣耀餐饮娱乐中心】您的验证码是: "+code,"","");
            logger.info(String.valueOf(result));
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (com.github.qcloudsms.httpclient.HTTPException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

}

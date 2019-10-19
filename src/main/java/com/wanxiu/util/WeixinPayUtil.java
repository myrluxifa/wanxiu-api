package com.wanxiu.util;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.net.ssl.SSLContext;

public class WeixinPayUtil {
	//微信参数配置  
    public static String API_KEY="wanxiurongyao123wanxiurongyao123";
    //随机字符串生成  
    public static String getRandomString(int length) { //length表示生成字符串的长度      
           String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";         
           Random random = new Random();         
           StringBuffer sb = new StringBuffer();         
           for (int i = 0; i < length; i++) {         
               int number = random.nextInt(base.length());
               sb.append(base.charAt(number));
           }         
           return sb.toString();         
        }    
    //请求xml组装  
      public static String getRequestXml(SortedMap<String,Object> parameters){  
            StringBuffer sb = new StringBuffer();  
            sb.append("<xml>");  
            Set es = parameters.entrySet();  
            Iterator it = es.iterator();  
            while(it.hasNext()) {  
                Map.Entry entry = (Map.Entry)it.next();  
                String key = (String)entry.getKey();  
                String value = (String)entry.getValue();  
                if ("attach".equalsIgnoreCase(key)||"body".equalsIgnoreCase(key)||"sign".equalsIgnoreCase(key)) {  
                    sb.append("<"+key+">"+"<![CDATA["+value+"]]></"+key+">");  
                }else {  
                    sb.append("<"+key+">"+value+"</"+key+">");  
                }  
            }  
            sb.append("</xml>");  
            return sb.toString();  
        }  
      //生成签名  
      public static String createSign(SortedMap<String,Object> parameters){  
            StringBuffer sb = new StringBuffer();  
            Set es = parameters.entrySet();  
            Iterator it = es.iterator();  
            while(it.hasNext()) {  
                Map.Entry entry = (Map.Entry)it.next();  
                String k = (String)entry.getKey();  
                Object v = entry.getValue();  
                if(null != v && !"".equals(v)  
                        && !"sign".equals(k) && !"key".equals(k)) {  
                    sb.append(k + "=" + v + "&");  
                }  
            }  
            sb.append("key=" + API_KEY);
            String sign= DigestUtils.md5Hex(sb.toString()).toUpperCase();
            return sign;  
        }  
      //验签
      public boolean verifyWeixinNotify(Map<Object, Object> map) {  
          SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();  
          String sign = (String) map.get("sign");  
          for (Object keyValue : map.keySet()) {  
              if(!keyValue.toString().equals("sign")){  
                  parameterMap.put(keyValue.toString(), map.get(keyValue));  
              }  
                
          }  
          String createSign = WeixinPayUtil.createSign(parameterMap);  
          if(createSign.equals(sign)){  
              return true;  
          }else{  
              return false;  
          }  
            
      }  
      //请求方法  
      public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {  
            try {  
                 
                URL url = new URL(requestUrl);  
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                
                conn.setDoOutput(true);  
                conn.setDoInput(true);  
                conn.setUseCaches(false);  
                // 设置请求方式（GET/POST）  
                conn.setRequestMethod(requestMethod);  
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  
                // 当outputStr不为null时向输出流写数据  
                if (null != outputStr) {  
                    OutputStream outputStream = conn.getOutputStream();  
                    // 注意编码格式  
                    outputStream.write(outputStr.getBytes("UTF-8"));  
                    outputStream.close();  
                }  
                // 从输入流读取返回内容  
                InputStream inputStream = conn.getInputStream();  
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
                String str = null;  
                StringBuffer buffer = new StringBuffer();  
                while ((str = bufferedReader.readLine()) != null) {  
                    buffer.append(str);  
                }  
                // 释放资源  
                bufferedReader.close();  
                inputStreamReader.close();  
                inputStream.close();  
                inputStream = null;  
                conn.disconnect();  
                return buffer.toString();  
            } catch (ConnectException ce) {  
                System.out.println("连接超时：{}"+ ce);  
            } catch (Exception e) {  
                System.out.println("https请求异常：{}"+ e);  
            }  
            return null;  
        }


    public static String doRefund(String mchId,String url, String data) throws Exception{
        /**
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
         */
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        //P12文件目录 证书路径，这里需要你自己修改，linux下还是windows下的根路径
        String filepath = "/home/";
        FileInputStream instream = new FileInputStream(filepath+"apiclient_cert.p12");
        try {
            keyStore.load(instream, mchId.toCharArray());//这里写密码..默认是你的MCHID
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mchId.toCharArray())//这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    //xml解析
      public static Map doXMLParse(String strxml) throws JDOMException, IOException {  
            strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");  
  
            if(null == strxml || "".equals(strxml)) {  
                return null;  
            }  
              
            Map m = new HashMap();  
              
            InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));  
            SAXBuilder builder = new SAXBuilder();  
            Document doc = builder.build(in);  
            Element root = doc.getRootElement();  
            List list = root.getChildren();  
            Iterator it = list.iterator();  
            while(it.hasNext()) {  
                Element e = (Element) it.next();
                String k = e.getName();  
                String v = "";  
                List children = e.getChildren();
                if(children.isEmpty()) {  
                    v = e.getTextNormalize();  
                } else {  
                    v = getChildrenText(children);  
                }  
                  
                m.put(k, v);  
            }  
              
            //关闭流  
            in.close();  
              
            return m;  
        }  
        
      public static String getChildrenText(List children) {  
            StringBuffer sb = new StringBuffer();  
            if(!children.isEmpty()) {  
                Iterator it = children.iterator();  
                while(it.hasNext()) {  
                    Element e = (Element) it.next();  
                    String name = e.getName();  
                    String value = e.getTextNormalize();  
                    List list = e.getChildren();  
                    sb.append("<" + name + ">");  
                    if(!list.isEmpty()) {  
                        sb.append(getChildrenText(list));  
                    }  
                    sb.append(value);  
                    sb.append("</" + name + ">");  
                }  
            }  
              
            return sb.toString();  
        }  
      
      
      public static String setXML(String return_code, String return_msg) {  
          return "<xml><return_code><![CDATA[" + return_code  
                  + "]]></return_code><return_msg><![CDATA[" + return_msg  
                  + "]]></return_msg></xml>";  

      }


      
      
}

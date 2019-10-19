package com.wanxiu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Util {
    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }

    public static Date notifyTimeFormate(String _date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = simpleDateFormat.parse(_date);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    public static String convertStringToUTF8(String s){
        if(s==null||s.equals("")) {
            return null;
        }
        StringBuffer sb=new StringBuffer();
        try{
            char c;
            for(int i=0;i<s.length();i++){
                c=s.charAt(i);
                if(c>=0&&c<=255){
                    sb.append(c);
                }else{
                    byte[] b;
                    b=Character.toString(c).getBytes("utf-8");
                    for(int j = 0; j < b.length; j++) {
                        int k=b[j];
                        k=k<0?k+256:k;
                        sb.append(Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }


}

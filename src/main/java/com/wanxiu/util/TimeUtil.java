package com.wanxiu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static String comouteAge(String birthday){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate= null;
        int age=20;
        try {
            birthDate = simpleDateFormat.parse(birthday);
            Calendar calendar=Calendar.getInstance();
            int t_year=calendar.get(Calendar.YEAR);
            int t_month=calendar.get(Calendar.MONTH);
            int t_day=calendar.get(Calendar.DAY_OF_MONTH);
            calendar.setTime(birthDate);
            int b_year=calendar.get(Calendar.YEAR);
            int b_month=calendar.get(Calendar.MONTH);
            int b_day=calendar.get(Calendar.DAY_OF_MONTH);

            age=t_year-b_year-1;
            //如果当前月份大于生日月份则年龄+1
            if(t_month>b_month){
                age=age++;
            }else if(t_month==b_month&&t_day>=b_day){//生日月份于当前月份相等，且当前日期大于等于生日日期则+1
                age=age++;
            }
            return String.valueOf(age);
        } catch (ParseException e) {
            e.printStackTrace();

        }finally {
            return String.valueOf(age);
        }
    }

    public static Date guardianEndTime(Date beginTime,int dayNum) {
        Date date;
        Calendar ca = Calendar.getInstance();
        ca.setTime(beginTime);
        ca.add(Calendar.DATE, dayNum);
        date = ca.getTime();
        return date;
    }


    public static String simpleTime(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

}

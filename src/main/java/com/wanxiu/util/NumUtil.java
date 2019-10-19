package com.wanxiu.util;

import java.util.Random;

public class NumUtil {

    public static Integer randomNum(int length){
        Random random= new Random();
        String R="";
        while(R.length()<length){
            R=R+random.nextInt(10);
        }
        return Integer.valueOf(R);
    }




}

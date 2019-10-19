package com.wanxiu.util;

import java.util.Random;
import java.util.Vector;

public class HongBaoUtil {

    public static Vector loadHongBao(int total,int count){
        Vector<Integer> vector = new Vector<>();

        if(count!=0&&total/count==1) {
            while(count>0) {
                vector.add(1);
                count--;
            }
            return vector;
        }

        if(count==0) {
            return vector;
        }


        while(count>=1) {
            //本次红包
            int money;
            //剩余
            int resultMoney;

            if(count==1) {
                vector.add(total);
                total=0;
                count--;
            }else {
                while(true) {
                    money=new Random().nextInt(total/count+total/count);
                    resultMoney=total-money;
                    if((resultMoney/(count-1))>=1&&money>0) {
                        break;
                    }
                }
                vector.add(money);
                total=total-money;
                count--;
            }

        }
        return vector;
    }
}
